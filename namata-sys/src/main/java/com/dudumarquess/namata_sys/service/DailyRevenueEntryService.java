package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.CreateDailyRevenueEntryRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateDailyRevenueEntryRequest;
import com.dudumarquess.namata_sys.dto.response.DailyRevenueEntryResponse;
import com.dudumarquess.namata_sys.dto.response.MonthlyDailyRevenueListResponse;
import com.dudumarquess.namata_sys.entity.DailyRevenueEntry;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import com.dudumarquess.namata_sys.repository.DailyRevenueEntryRepository;
import com.dudumarquess.namata_sys.repository.MonthlyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyRevenueEntryService {

	private final DailyRevenueEntryRepository dailyRevenueEntryRepository;
	private final MonthlyRecordRepository monthlyRecordRepository;

	public DailyRevenueEntryService(
			DailyRevenueEntryRepository dailyRevenueEntryRepository,
			MonthlyRecordRepository monthlyRecordRepository
	) {
		this.dailyRevenueEntryRepository = dailyRevenueEntryRepository;
		this.monthlyRecordRepository = monthlyRecordRepository;
	}

	@Transactional
	public ServiceResult<DailyRevenueEntryResponse> create(CreateDailyRevenueEntryRequest request) {
		List<ApiFieldError> errors = validateCreateRequest(request);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid daily revenue payload", errors, 400);
		}

		MonthlyRecord monthlyRecord = monthlyRecordRepository
				.findByYearAndMonth(request.year(), request.month())
				.orElse(null);
		if (monthlyRecord == null) {
			return ServiceResult.failure(
					"Monthly record not found",
					List.of(new ApiFieldError("month", "No monthly record found for the provided year and month")),
					404
			);
		}

		DailyRevenueEntry duplicate = dailyRevenueEntryRepository
				.findByMonthlyRecordAndDay(monthlyRecord, request.day())
				.orElse(null);
		if (duplicate != null) {
			return ServiceResult.failure(
					"Daily revenue entry already exists for this day",
					List.of(new ApiFieldError("day", "A revenue entry already exists for this month and day")),
					409
			);
		}

		DailyRevenueEntry entry = new DailyRevenueEntry();
		entry.setMonthlyRecord(monthlyRecord);
		entry.setDay(request.day());
		entry.setCashAmount(request.cashAmount());
		entry.setMultibancoAmount(request.multibancoAmount());
		entry.setAppsGrossAmount(request.appsGrossAmount());
		entry.setOtherIncomeAmount(request.otherIncomeAmount());
		entry.setAppFeePercentageUsed(request.appFeePercentageUsed());
		entry.calculateOfficialAmount();
		entry.calculateRealAmount();

		DailyRevenueEntry saved = dailyRevenueEntryRepository.save(entry);
		return ServiceResult.success("Daily revenue entry created successfully", toResponse(saved), 201);
	}

	@Transactional
	public ServiceResult<DailyRevenueEntryResponse> update(Long id, UpdateDailyRevenueEntryRequest request) {
		if (id == null) {
			return ServiceResult.failure("Invalid daily revenue id", List.of(new ApiFieldError("id", "Id is required")), 400);
		}

		List<ApiFieldError> errors = validateUpdateRequest(request);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid daily revenue payload", errors, 400);
		}

		DailyRevenueEntry entry = dailyRevenueEntryRepository.findById(id).orElse(null);
		if (entry == null) {
			return ServiceResult.failure("Daily revenue entry not found", 404);
		}

		DailyRevenueEntry duplicate = dailyRevenueEntryRepository
				.findByMonthlyRecordAndDay(entry.getMonthlyRecord(), request.day())
				.orElse(null);
		if (duplicate != null && !duplicate.getId().equals(id)) {
			return ServiceResult.failure(
					"Daily revenue entry already exists for this day",
					List.of(new ApiFieldError("day", "A revenue entry already exists for this month and day")),
					409
			);
		}

		entry.setDay(request.day());
		entry.setCashAmount(request.cashAmount());
		entry.setMultibancoAmount(request.multibancoAmount());
		entry.setAppsGrossAmount(request.appsGrossAmount());
		entry.setOtherIncomeAmount(request.otherIncomeAmount());
		entry.setAppFeePercentageUsed(request.appFeePercentageUsed());
		entry.calculateOfficialAmount();
		entry.calculateRealAmount();

		DailyRevenueEntry saved = dailyRevenueEntryRepository.save(entry);
		return ServiceResult.success("Daily revenue entry updated successfully", toResponse(saved), 200);
	}

	@Transactional(readOnly = true)
	public ServiceResult<MonthlyDailyRevenueListResponse> getByMonth(Integer year, Integer month) {
		List<ApiFieldError> errors = validateYearMonth(year, month);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid month parameters", errors, 400);
		}

		MonthlyRecord monthlyRecord = monthlyRecordRepository.findByYearAndMonth(year, month).orElse(null);
		if (monthlyRecord == null) {
			return ServiceResult.failure("Monthly record not found", 404);
		}

		List<DailyRevenueEntryResponse> responses = dailyRevenueEntryRepository
				.findByMonthlyRecordOrderByDayAsc(monthlyRecord)
				.stream()
				.map(this::toResponse)
				.toList();

		MonthlyDailyRevenueListResponse response = new MonthlyDailyRevenueListResponse(year, month, responses);
		return ServiceResult.success("Daily revenues loaded successfully", response, 200);
	}

	private List<ApiFieldError> validateCreateRequest(CreateDailyRevenueEntryRequest request) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (request == null) {
			errors.add(new ApiFieldError("request", "Request body is required"));
			return errors;
		}
		errors.addAll(validateYearMonth(request.year(), request.month()));
		validateDayAndMonetary(
				request.day(),
				request.cashAmount(),
				request.multibancoAmount(),
				request.appsGrossAmount(),
				request.otherIncomeAmount(),
				request.appFeePercentageUsed(),
				errors
		);
		return errors;
	}

	private List<ApiFieldError> validateUpdateRequest(UpdateDailyRevenueEntryRequest request) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (request == null) {
			errors.add(new ApiFieldError("request", "Request body is required"));
			return errors;
		}
		validateDayAndMonetary(
				request.day(),
				request.cashAmount(),
				request.multibancoAmount(),
				request.appsGrossAmount(),
				request.otherIncomeAmount(),
				request.appFeePercentageUsed(),
				errors
		);
		return errors;
	}

	private void validateDayAndMonetary(
			Integer day,
			BigDecimal cash,
			BigDecimal multibanco,
			BigDecimal appsGross,
			BigDecimal otherIncome,
			BigDecimal appFee,
			List<ApiFieldError> errors
	) {
		if (day == null || day < 1 || day > 31) {
			errors.add(new ApiFieldError("day", "Day must be between 1 and 31"));
		}
		if (cash == null || cash.compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new ApiFieldError("cashAmount", "Cash amount must be greater than or equal to 0.00"));
		}
		if (multibanco == null || multibanco.compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new ApiFieldError("multibancoAmount", "Multibanco amount must be greater than or equal to 0.00"));
		}
		if (appsGross == null || appsGross.compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new ApiFieldError("appsGrossAmount", "Apps gross amount must be greater than or equal to 0.00"));
		}
		if (otherIncome == null || otherIncome.compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new ApiFieldError("otherIncomeAmount", "Other income amount must be greater than or equal to 0.00"));
		}
		if (appFee == null || appFee.compareTo(BigDecimal.ZERO) < 0 || appFee.compareTo(BigDecimal.valueOf(100)) > 0) {
			errors.add(new ApiFieldError("appFeePercentageUsed", "App fee percentage must be between 0.00 and 100.00"));
		}
	}

	private List<ApiFieldError> validateYearMonth(Integer year, Integer month) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (year == null || year < 2000) {
			errors.add(new ApiFieldError("year", "Year must be greater than or equal to 2000"));
		}
		if (month == null || month < 1 || month > 12) {
			errors.add(new ApiFieldError("month", "Month must be between 1 and 12"));
		}
		return errors;
	}

	private DailyRevenueEntryResponse toResponse(DailyRevenueEntry entry) {
		return new DailyRevenueEntryResponse(
				entry.getId(),
				entry.getMonthlyRecord().getId(),
				entry.getDay(),
				entry.getCashAmount(),
				entry.getMultibancoAmount(),
				entry.getAppsGrossAmount(),
				entry.getOtherIncomeAmount(),
				entry.getAppFeePercentageUsed(),
				entry.getOfficialAmount(),
				entry.getRealAmount()
		);
	}

}
