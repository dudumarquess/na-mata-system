package com.dudumarquess.namata_sys.controller;

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
    public ResponseEntity<MonthlySummaryResponse> getSummary(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ResponseEntity.ok(monthlySummaryService.getSummary(year, month));
    }
}

