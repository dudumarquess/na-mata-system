package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.response.MonthlySummaryResponse;
import com.dudumarquess.namata_sys.entity.DailyRevenueEntry;
import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import com.dudumarquess.namata_sys.repository.DailyRevenueEntryRepository;
import com.dudumarquess.namata_sys.repository.ExpenseEntryRepository;
import com.dudumarquess.namata_sys.repository.MonthlyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlySummaryService {

	private final MonthlyRecordRepository monthlyRecordRepository;
	private final DailyRevenueEntryRepository dailyRevenueEntryRepository;
	private final ExpenseEntryRepository expenseEntryRepository;

	public MonthlySummaryService(
			MonthlyRecordRepository monthlyRecordRepository,
			DailyRevenueEntryRepository dailyRevenueEntryRepository,
			ExpenseEntryRepository expenseEntryRepository
	) {
		this.monthlyRecordRepository = monthlyRecordRepository;
		this.dailyRevenueEntryRepository = dailyRevenueEntryRepository;
		this.expenseEntryRepository = expenseEntryRepository;
	}

	@Transactional(readOnly = true)
	public ServiceResult<MonthlySummaryResponse> getSummary(Integer year, Integer month) {
		List<ApiFieldError> errors = validateYearMonth(year, month);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid month parameters", errors, 400);
		}

		MonthlyRecord monthlyRecord = monthlyRecordRepository.findByYearAndMonth(year, month).orElse(null);
		if (monthlyRecord == null) {
			return ServiceResult.failure("Monthly record not found", 404);
		}

		List<DailyRevenueEntry> revenues = dailyRevenueEntryRepository.findByMonthlyRecordOrderByDayAsc(monthlyRecord);
		List<ExpenseEntry> expenses = expenseEntryRepository.findByMonthlyRecordOrderByDayAsc(monthlyRecord);

		BigDecimal totalCash = revenues.stream()
				.map(DailyRevenueEntry::getCashAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalMultibanco = revenues.stream()
				.map(DailyRevenueEntry::getMultibancoAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalAppsGross = revenues.stream()
				.map(DailyRevenueEntry::getAppsGrossAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalOtherIncome = revenues.stream()
				.map(DailyRevenueEntry::getOtherIncomeAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalOfficial = revenues.stream()
				.map(DailyRevenueEntry::getOfficialAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalReal = revenues.stream()
				.map(DailyRevenueEntry::getRealAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalExpenses = expenses.stream()
				.map(ExpenseEntry::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal balance = totalReal.subtract(totalExpenses);

		MonthlySummaryResponse response = new MonthlySummaryResponse(
				year,
				month,
				normalize(totalCash),
				normalize(totalMultibanco),
				normalize(totalAppsGross),
				normalize(totalOtherIncome),
				normalize(totalOfficial),
				normalize(totalReal),
				normalize(totalExpenses),
				normalize(balance)
		);

		return ServiceResult.success("Monthly summary loaded successfully", response, 200);
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

	private BigDecimal normalize(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}
}
