package com.dudumarquess.namata_sys.dto.response;

import java.math.BigDecimal;

public record FixedExpenseTemplateResponse(
        Long id,
        String name,
        BigDecimal defaultAmount,
        Integer defaultDay,
        Boolean active,
        Boolean autoLaunchEnabled
) {
}

