package com.dudumarquess.namata_sys.dto.response;

import java.math.BigDecimal;

public record DailyRevenueEntryResponse(
        Long id,
        Long monthlyRecordId,
        Integer day,
        BigDecimal cashAmount,
        BigDecimal multibancoAmount,
        BigDecimal appsGrossAmount,
        BigDecimal appFeePercentageUsed,
        BigDecimal officialAmount,
        BigDecimal realAmount
) {
}

