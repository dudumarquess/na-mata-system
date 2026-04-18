package com.dudumarquess.namata_sys.dto.response;

import java.util.List;

public record FixedExpenseTemplateListResponse(
        List<FixedExpenseTemplateResponse> templates
) {
}

