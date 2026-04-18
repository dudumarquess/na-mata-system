package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.dto.request.CreateFixedExpenseTemplateRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateFixedExpenseTemplateRequest;
import com.dudumarquess.namata_sys.dto.response.FixedExpenseTemplateListResponse;
import com.dudumarquess.namata_sys.dto.response.FixedExpenseTemplateResponse;
import com.dudumarquess.namata_sys.service.FixedExpenseTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fixed-expense-templates")
public class FixedExpenseTemplateController {

    private final FixedExpenseTemplateService fixedExpenseTemplateService;

    public FixedExpenseTemplateController(FixedExpenseTemplateService fixedExpenseTemplateService) {
        this.fixedExpenseTemplateService = fixedExpenseTemplateService;
    }

    @PostMapping
    public ResponseEntity<FixedExpenseTemplateResponse> create(
            @Valid @RequestBody CreateFixedExpenseTemplateRequest request
    ) {
        return ResponseEntity.ok(fixedExpenseTemplateService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FixedExpenseTemplateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFixedExpenseTemplateRequest request
    ) {
        return ResponseEntity.ok(fixedExpenseTemplateService.update(id, request));
    }

    @GetMapping
    public ResponseEntity<FixedExpenseTemplateListResponse> getAll() {
        return ResponseEntity.ok(fixedExpenseTemplateService.getAll());
    }
}

