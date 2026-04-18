package com.dudumarquess.namata_sys.dto.response;

import java.util.List;

public record MonthlyExpenseListResponse(
        Integer year,
        Integer month,
        List<ExpenseEntryResponse> entries
) {
}

