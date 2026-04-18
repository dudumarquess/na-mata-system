package com.dudumarquess.namata_sys.controller;

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

@RestController
@RequestMapping("/api/daily-revenues")
public class DailyRevenueEntryController {

    private final DailyRevenueEntryService dailyRevenueEntryService;

    public DailyRevenueEntryController(DailyRevenueEntryService dailyRevenueEntryService) {
        this.dailyRevenueEntryService = dailyRevenueEntryService;
    }

    @PostMapping
    public ResponseEntity<DailyRevenueEntryResponse> create(@Valid @RequestBody CreateDailyRevenueEntryRequest request) {
        return ResponseEntity.ok(dailyRevenueEntryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyRevenueEntryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDailyRevenueEntryRequest request
    ) {
        return ResponseEntity.ok(dailyRevenueEntryService.update(id, request));
    }

    @GetMapping
    public ResponseEntity<MonthlyDailyRevenueListResponse> getByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ResponseEntity.ok(dailyRevenueEntryService.getByMonth(year, month));
    }
}

