package com.dudumarquess.namata_sys.dto.response;

import com.dudumarquess.namata_sys.entity.enums.MonthStatus;

public record MonthlyRecordResponse(
        Long id,
        Integer year,
        Integer month,
        MonthStatus status
) {
}

