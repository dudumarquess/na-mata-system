package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.dto.request.CreateExpenseEntryRequest;
import com.dudumarquess.namata_sys.dto.response.ExpenseEntryResponse;
import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEntryService {

    public ExpenseEntryResponse create(CreateExpenseEntryRequest request) {
        ExpenseEntry expenseEntry = new ExpenseEntry();

    }
}
