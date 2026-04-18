package com.dudumarquess.namata_sys.dto.response;

import java.math.BigDecimal;

public record SystemSettingsResponse(
        Long id,
        BigDecimal defaultAppFeePercentage
) {
}

