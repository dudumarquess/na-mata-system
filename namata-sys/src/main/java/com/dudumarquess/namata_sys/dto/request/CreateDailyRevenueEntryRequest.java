package com.dudumarquess.namata_sys.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateDailyRevenueEntryRequest(
        @NotNull @Min(1) Integer year,
        @NotNull @Min(1) @Max(12) Integer month,
        @NotNull @Min(1) @Max(31) Integer day,
        @NotNull @DecimalMin("0.00") BigDecimal cashAmount,
        @NotNull @DecimalMin("0.00") BigDecimal multibancoAmount,
        @NotNull @DecimalMin("0.00") BigDecimal appsGrossAmount,
        @NotNull @DecimalMin("0.00") BigDecimal otherIncomeAmount,
        @DecimalMin("0.00") @jakarta.validation.constraints.DecimalMax("100.00") BigDecimal appFeePercentageUsed
) {
}
