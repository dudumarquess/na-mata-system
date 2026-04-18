package com.dudumarquess.namata_sys.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class DecimalScaleNormalizer {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private DecimalScaleNormalizer() {
    }

    static BigDecimal normalize(BigDecimal value) {
        return value == null ? null : value.setScale(SCALE, ROUNDING_MODE);
    }
}

