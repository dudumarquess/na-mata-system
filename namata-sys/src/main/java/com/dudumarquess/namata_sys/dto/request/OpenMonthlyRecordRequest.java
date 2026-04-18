package com.dudumarquess.namata_sys.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OpenMonthlyRecordRequest(
        @NotNull @Min(1) Integer year,
        @NotNull @Min(1) @Max(12) Integer month
) {
}

