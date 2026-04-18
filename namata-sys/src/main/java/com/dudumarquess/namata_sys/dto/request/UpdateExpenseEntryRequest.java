package com.dudumarquess.namata_sys.dto.request;

import com.dudumarquess.namata_sys.entity.enums.ExpenseNature;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateExpenseEntryRequest(
        @NotNull @Min(1) @Max(31) Integer day,
        @NotBlank String description,
        @NotNull @DecimalMin("0.00") BigDecimal amount,
        @NotNull ExpenseNature expenseType,
        Long fixedExpenseTemplateId
) {
}

