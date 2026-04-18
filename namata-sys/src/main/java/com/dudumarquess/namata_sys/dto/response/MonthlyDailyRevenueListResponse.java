package com.dudumarquess.namata_sys.dto.response;

import java.util.List;

public record MonthlyDailyRevenueListResponse(
        Integer year,
        Integer month,
        List<DailyRevenueEntryResponse> entries
) {
}

