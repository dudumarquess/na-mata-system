package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Long> {
}
