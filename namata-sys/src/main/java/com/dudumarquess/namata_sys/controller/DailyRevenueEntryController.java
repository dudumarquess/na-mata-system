package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.api.ApiResponse;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.CreateDailyRevenueEntryRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateDailyRevenueEntryRequest;
import com.dudumarquess.namata_sys.dto.response.DailyRevenueEntryResponse;
import com.dudumarquess.namata_sys.dto.response.MonthlyDailyRevenueListResponse;
import com.dudumarquess.namata_sys.service.DailyRevenueEntryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/daily-revenues")
public class DailyRevenueEntryController {

    private final DailyRevenueEntryService dailyRevenueEntryService;

    public DailyRevenueEntryController(DailyRevenueEntryService dailyRevenueEntryService) {
        this.dailyRevenueEntryService = dailyRevenueEntryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DailyRevenueEntryResponse>> create(@Valid @RequestBody CreateDailyRevenueEntryRequest request) {
        // Apply default value for appFeePercentageUsed if null
        if (request.appFeePercentageUsed() == null) {
            request = new CreateDailyRevenueEntryRequest(
                    request.year(),
                    request.month(),
                    request.day(),
                    request.cashAmount(),
                    request.multibancoAmount(),
                    request.appsGrossAmount(),
                    request.otherIncomeAmount(),
                    new BigDecimal("30.00")
            );
        }
        return toResponse(dailyRevenueEntryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DailyRevenueEntryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDailyRevenueEntryRequest request
    ) {
        // Apply default value for appFeePercentageUsed if null
        UpdateDailyRevenueEntryRequest updatedRequest = request;
        if (request.appFeePercentageUsed() == null) {
            updatedRequest = new UpdateDailyRevenueEntryRequest(
                    request.day(),
                    request.cashAmount(),
                    request.multibancoAmount(),
                    request.appsGrossAmount(),
                    request.otherIncomeAmount(),
                    new BigDecimal("30.00")
            );
        }
        return toResponse(dailyRevenueEntryService.update(id, updatedRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MonthlyDailyRevenueListResponse>> getByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return toResponse(dailyRevenueEntryService.getByMonth(year, month));
    }

    private <T> ResponseEntity<ApiResponse<T>> toResponse(ServiceResult<T> result) {
        ApiResponse<T> response = result.success()
                ? ApiResponse.success(result.message(), result.data())
                : ApiResponse.failure(result.message(), result.errors());
        return ResponseEntity.status(result.statusCode()).body(response);
    }
}
