package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.OpenMonthlyRecordRequest;
import com.dudumarquess.namata_sys.dto.response.MonthlyRecordResponse;
import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import com.dudumarquess.namata_sys.entity.FixedExpenseTemplate;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import com.dudumarquess.namata_sys.entity.enums.ExpenseNature;
import com.dudumarquess.namata_sys.entity.enums.ExpenseSourceType;
import com.dudumarquess.namata_sys.entity.enums.MonthStatus;
import com.dudumarquess.namata_sys.repository.ExpenseEntryRepository;
import com.dudumarquess.namata_sys.repository.FixedExpenseTemplateRepository;
import com.dudumarquess.namata_sys.repository.MonthlyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlyRecordService {

	private final MonthlyRecordRepository monthlyRecordRepository;
	private final FixedExpenseTemplateRepository fixedExpenseTemplateRepository;
	private final ExpenseEntryRepository expenseEntryRepository;

	public MonthlyRecordService(
			MonthlyRecordRepository monthlyRecordRepository,
			FixedExpenseTemplateRepository fixedExpenseTemplateRepository,
			ExpenseEntryRepository expenseEntryRepository
	) {
		this.monthlyRecordRepository = monthlyRecordRepository;
		this.fixedExpenseTemplateRepository = fixedExpenseTemplateRepository;
		this.expenseEntryRepository = expenseEntryRepository;
	}

	@Transactional
	public ServiceResult<MonthlyRecordResponse> openOrCreateMonth(OpenMonthlyRecordRequest request) {
		List<ApiFieldError> errors = validateRequest(request);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid month payload", errors, 400);
		}

		MonthlyRecord existing = monthlyRecordRepository
				.findByYearAndMonth(request.year(), request.month())
				.orElse(null);

		if (existing != null) {
			return ServiceResult.success("Monthly record loaded successfully", toResponse(existing), 200);
		}

		MonthlyRecord monthlyRecord = new MonthlyRecord();
		monthlyRecord.setYear(request.year());
		monthlyRecord.setMonth(request.month());
		monthlyRecord.setStatus(MonthStatus.OPEN);
		MonthlyRecord saved = monthlyRecordRepository.save(monthlyRecord);

		List<FixedExpenseTemplate> templates = fixedExpenseTemplateRepository.findByActiveTrueAndAutoLaunchEnabledTrue();
		for (FixedExpenseTemplate template : templates) {
			ExpenseEntry entry = new ExpenseEntry();
			entry.setMonthlyRecord(saved);
			entry.setDay(template.getDefaultDay());
			entry.setDescription(template.getName());
			entry.setAmount(template.getDefaultAmount());
			entry.setExpenseNature(ExpenseNature.FIXED);
			entry.setSourceType(ExpenseSourceType.AUTO_GENERATED_FIXED);
			entry.setFixedExpenseTemplate(template);
			expenseEntryRepository.save(entry);
		}

		return ServiceResult.success("Monthly record created successfully", toResponse(saved), 201);
	}

	private List<ApiFieldError> validateRequest(OpenMonthlyRecordRequest request) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (request == null) {
			errors.add(new ApiFieldError("request", "Request body is required"));
			return errors;
		}
		if (request.year() == null) {
			errors.add(new ApiFieldError("year", "Year is required"));
		} else if (request.year() < 2000) {
			errors.add(new ApiFieldError("year", "Year must be greater than or equal to 2000"));
		}

		if (request.month() == null) {
			errors.add(new ApiFieldError("month", "Month is required"));
		} else if (request.month() < 1 || request.month() > 12) {
			errors.add(new ApiFieldError("month", "Month must be between 1 and 12"));
		}
		return errors;
	}

	private MonthlyRecordResponse toResponse(MonthlyRecord monthlyRecord) {
		return new MonthlyRecordResponse(
				monthlyRecord.getId(),
				monthlyRecord.getYear(),
				monthlyRecord.getMonth(),
				monthlyRecord.getStatus()
		);
	}
}
