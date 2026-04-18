package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.CreateExpenseEntryRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateExpenseEntryRequest;
import com.dudumarquess.namata_sys.dto.response.ExpenseEntryResponse;
import com.dudumarquess.namata_sys.dto.response.MonthlyExpenseListResponse;
import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import com.dudumarquess.namata_sys.entity.FixedExpenseTemplate;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import com.dudumarquess.namata_sys.entity.enums.ExpenseSourceType;
import com.dudumarquess.namata_sys.repository.ExpenseEntryRepository;
import com.dudumarquess.namata_sys.repository.FixedExpenseTemplateRepository;
import com.dudumarquess.namata_sys.repository.MonthlyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseEntryService {

    private final ExpenseEntryRepository expenseEntryRepository;
    private final MonthlyRecordRepository monthlyRecordRepository;
    private final FixedExpenseTemplateRepository fixedExpenseTemplateRepository;

    public ExpenseEntryService(
            ExpenseEntryRepository expenseEntryRepository,
            MonthlyRecordRepository monthlyRecordRepository,
            FixedExpenseTemplateRepository fixedExpenseTemplateRepository
    ) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.monthlyRecordRepository = monthlyRecordRepository;
        this.fixedExpenseTemplateRepository = fixedExpenseTemplateRepository;
    }

    @Transactional
    public ServiceResult<ExpenseEntryResponse> create(CreateExpenseEntryRequest request) {
        List<ApiFieldError> errors = validateCreateRequest(request);
        if (!errors.isEmpty()) {
            return ServiceResult.failure("Invalid expense payload", errors, 400);
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

        FixedExpenseTemplate template = resolveTemplate(request.fixedExpenseTemplateId());
        if (request.fixedExpenseTemplateId() != null && template == null) {
            return ServiceResult.failure(
                    "Fixed expense template not found",
                    List.of(new ApiFieldError("fixedExpenseTemplateId", "Fixed expense template does not exist")),
                    404
            );
        }

        ExpenseEntry entry = new ExpenseEntry();
        entry.setMonthlyRecord(monthlyRecord);
        entry.setDay(request.day());
        entry.setDescription(request.description().trim());
        entry.setAmount(request.amount());
        entry.setExpenseNature(request.expenseType());
        entry.setFixedExpenseTemplate(template);
        entry.setSourceType(template == null ? ExpenseSourceType.MANUAL : ExpenseSourceType.AUTO_GENERATED_FIXED);

        ExpenseEntry saved = expenseEntryRepository.save(entry);
        return ServiceResult.success("Expense entry created successfully", toResponse(saved), 201);
    }

    @Transactional
    public ServiceResult<ExpenseEntryResponse> update(Long id, UpdateExpenseEntryRequest request) {
        if (id == null) {
            return ServiceResult.failure("Invalid expense id", List.of(new ApiFieldError("id", "Id is required")), 400);
        }

        List<ApiFieldError> errors = validateUpdateRequest(request);
        if (!errors.isEmpty()) {
            return ServiceResult.failure("Invalid expense payload", errors, 400);
        }

        ExpenseEntry entry = expenseEntryRepository.findById(id).orElse(null);
        if (entry == null) {
            return ServiceResult.failure("Expense entry not found", 404);
        }

        FixedExpenseTemplate template = resolveTemplate(request.fixedExpenseTemplateId());
        if (request.fixedExpenseTemplateId() != null && template == null) {
            return ServiceResult.failure(
                    "Fixed expense template not found",
                    List.of(new ApiFieldError("fixedExpenseTemplateId", "Fixed expense template does not exist")),
                    404
            );
        }

        BigDecimal previousAmount = entry.getAmount();

        entry.setDay(request.day());
        entry.setDescription(request.description().trim());
        entry.setAmount(request.amount());
        entry.setExpenseNature(request.expenseType());
        entry.setFixedExpenseTemplate(template);
        entry.setSourceType(template == null ? ExpenseSourceType.MANUAL : ExpenseSourceType.AUTO_GENERATED_FIXED);

        ExpenseEntry saved = expenseEntryRepository.save(entry);

        if (saved.getFixedExpenseTemplate() != null
                && saved.getSourceType() == ExpenseSourceType.AUTO_GENERATED_FIXED
                && previousAmount != null
                && previousAmount.compareTo(saved.getAmount()) != 0) {
            FixedExpenseTemplate fixedTemplate = saved.getFixedExpenseTemplate();
            fixedTemplate.setDefaultAmount(saved.getAmount());
            fixedExpenseTemplateRepository.save(fixedTemplate);
        }

        return ServiceResult.success("Expense entry updated successfully", toResponse(saved), 200);
    }

    @Transactional
    public ServiceResult<Void> delete(Long id) {
        if (id == null) {
            return ServiceResult.failure("Invalid expense id", List.of(new ApiFieldError("id", "Id is required")), 400);
        }

        ExpenseEntry entry = expenseEntryRepository.findById(id).orElse(null);
        if (entry == null) {
            return ServiceResult.failure("Expense entry not found", 404);
        }

        expenseEntryRepository.delete(entry);
        return ServiceResult.success("Expense entry deleted successfully", null, 200);
    }

    @Transactional(readOnly = true)
    public ServiceResult<MonthlyExpenseListResponse> getByMonth(Integer year, Integer month) {
        List<ApiFieldError> errors = validateYearMonth(year, month);
        if (!errors.isEmpty()) {
            return ServiceResult.failure("Invalid month parameters", errors, 400);
        }

        MonthlyRecord monthlyRecord = monthlyRecordRepository.findByYearAndMonth(year, month).orElse(null);
        if (monthlyRecord == null) {
            return ServiceResult.failure("Monthly record not found", 404);
        }

        List<ExpenseEntryResponse> entries = expenseEntryRepository
                .findByMonthlyRecordOrderByDayAsc(monthlyRecord)
                .stream()
                .map(this::toResponse)
                .toList();

        return ServiceResult.success(
                "Expenses loaded successfully",
                new MonthlyExpenseListResponse(year, month, entries),
                200
        );
    }

    private List<ApiFieldError> validateCreateRequest(CreateExpenseEntryRequest request) {
        List<ApiFieldError> errors = new ArrayList<>();
        if (request == null) {
            errors.add(new ApiFieldError("request", "Request body is required"));
            return errors;
        }
        errors.addAll(validateYearMonth(request.year(), request.month()));
        validateEntryFields(request.day(), request.description(), request.amount(), request.expenseType(), errors);
        return errors;
    }

    private List<ApiFieldError> validateUpdateRequest(UpdateExpenseEntryRequest request) {
        List<ApiFieldError> errors = new ArrayList<>();
        if (request == null) {
            errors.add(new ApiFieldError("request", "Request body is required"));
            return errors;
        }
        validateEntryFields(request.day(), request.description(), request.amount(), request.expenseType(), errors);
        return errors;
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

    private void validateEntryFields(
            Integer day,
            String description,
            BigDecimal amount,
            Object expenseType,
            List<ApiFieldError> errors
    ) {
        if (day == null || day < 1 || day > 31) {
            errors.add(new ApiFieldError("day", "Day must be between 1 and 31"));
        }
        if (description == null || description.isBlank()) {
            errors.add(new ApiFieldError("description", "Description is required"));
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            errors.add(new ApiFieldError("amount", "Amount must be greater than or equal to 0.00"));
        }
        if (expenseType == null) {
            errors.add(new ApiFieldError("expenseType", "Expense type is required"));
        }
    }

    private FixedExpenseTemplate resolveTemplate(Long fixedExpenseTemplateId) {
        if (fixedExpenseTemplateId == null) {
            return null;
        }
        return fixedExpenseTemplateRepository.findById(fixedExpenseTemplateId).orElse(null);
    }

    private ExpenseEntryResponse toResponse(ExpenseEntry entry) {
        return new ExpenseEntryResponse(
                entry.getId(),
                entry.getMonthlyRecord().getId(),
                entry.getDay(),
                entry.getDescription(),
                entry.getAmount(),
                entry.getExpenseNature(),
                entry.getFixedExpenseTemplate() == null ? null : entry.getFixedExpenseTemplate().getId()
        );
    }
}
