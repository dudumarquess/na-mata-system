package com.dudumarquess.namata_sys.dto.request;

import com.dudumarquess.namata_sys.entity.enums.ExpenseNature;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateExpenseEntryRequest(
        Integer year,
        Integer month,
        Integer day,
        String description,
        BigDecimal amount,
        ExpenseNature expenseType,
        Long fixedExpenseTemplateId
) {
}

