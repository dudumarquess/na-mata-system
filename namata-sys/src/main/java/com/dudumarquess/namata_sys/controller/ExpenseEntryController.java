package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.api.ApiResponse;
import com.dudumarquess.namata_sys.api.ServiceResult;
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
    public ResponseEntity<ApiResponse<ExpenseEntryResponse>> create(@Valid @RequestBody CreateExpenseEntryRequest request) {
        return toResponse(expenseEntryService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseEntryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseEntryRequest request
    ) {
        return toResponse(expenseEntryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return toResponse(expenseEntryService.delete(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MonthlyExpenseListResponse>> getByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return toResponse(expenseEntryService.getByMonth(year, month));
    }

    private <T> ResponseEntity<ApiResponse<T>> toResponse(ServiceResult<T> result) {
        ApiResponse<T> response = result.success()
                ? ApiResponse.success(result.message(), result.data())
                : ApiResponse.failure(result.message(), result.errors());
        return ResponseEntity.status(result.statusCode()).body(response);
    }
}

