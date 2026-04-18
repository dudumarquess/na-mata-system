package com.dudumarquess.namata_sys.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateFixedExpenseTemplateRequest(
        @NotBlank String name,
        @NotNull @DecimalMin("0.00") BigDecimal defaultAmount,
        @NotNull @Min(1) @Max(31) Integer defaultDay,
        @NotNull Boolean active,
        @NotNull Boolean autoLaunchEnabled
) {
}

