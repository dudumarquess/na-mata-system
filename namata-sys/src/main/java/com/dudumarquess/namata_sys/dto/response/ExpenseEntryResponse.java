package com.dudumarquess.namata_sys.dto.response;

import com.dudumarquess.namata_sys.entity.enums.ExpenseNature;

import java.math.BigDecimal;

public record ExpenseEntryResponse(
        Long id,
        Long monthlyRecordId,
        Integer day,
        String description,
        BigDecimal amount,
        ExpenseNature expenseType,
        Long fixedExpenseTemplateId
) {
}

