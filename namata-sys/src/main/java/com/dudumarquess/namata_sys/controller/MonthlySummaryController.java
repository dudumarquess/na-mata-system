package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.api.ApiResponse;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.response.MonthlySummaryResponse;
import com.dudumarquess.namata_sys.service.MonthlySummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monthly-summaries")
public class MonthlySummaryController {

    private final MonthlySummaryService monthlySummaryService;

    public MonthlySummaryController(MonthlySummaryService monthlySummaryService) {
        this.monthlySummaryService = monthlySummaryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MonthlySummaryResponse>> getSummary(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return toResponse(monthlySummaryService.getSummary(year, month));
    }

    private <T> ResponseEntity<ApiResponse<T>> toResponse(ServiceResult<T> result) {
        ApiResponse<T> response = result.success()
                ? ApiResponse.success(result.message(), result.data())
                : ApiResponse.failure(result.message(), result.errors());
        return ResponseEntity.status(result.statusCode()).body(response);
    }
}

