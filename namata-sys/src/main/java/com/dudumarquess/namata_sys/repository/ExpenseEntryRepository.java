package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.ExpenseEntry;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Long> {
	List<ExpenseEntry> findByMonthlyRecordOrderByDayAsc(MonthlyRecord monthlyRecord);
}
