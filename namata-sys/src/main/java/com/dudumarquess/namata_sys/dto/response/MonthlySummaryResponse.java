package com.dudumarquess.namata_sys.dto.response;

import java.math.BigDecimal;

public record MonthlySummaryResponse(
        Integer year,
        Integer month,
        BigDecimal totalCash,
        BigDecimal totalMultibanco,
        BigDecimal totalAppsGross,
        BigDecimal totalOtherIncome,
        BigDecimal totalOfficial,
        BigDecimal totalReal,
        BigDecimal totalExpenses,
        BigDecimal balance
) {
}
