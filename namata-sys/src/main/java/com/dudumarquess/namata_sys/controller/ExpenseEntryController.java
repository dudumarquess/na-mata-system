package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.dto.request.CreateExpenseEntryRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateExpenseEntryRequest;
import com.dudumarquess.namata_sys.dto.response.ExpenseEntryResponse;
import com.dudumarquess.namata_sys.dto.response.MonthlyExpenseListResponse;
import com.dudumarquess.namata_sys.service.ExpenseEntryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseEntryController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @PostMapping
    public ResponseEntity<ExpenseEntryResponse> create(@RequestBody CreateExpenseEntryRequest request) {
        
        return ResponseEntity.ok(expenseEntryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseEntryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseEntryRequest request
    ) {
        return ResponseEntity.ok(expenseEntryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<MonthlyExpenseListResponse> getByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ResponseEntity.ok(expenseEntryService.getByMonth(year, month));
    }
}

