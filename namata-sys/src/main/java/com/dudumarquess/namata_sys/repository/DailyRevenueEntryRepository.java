package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.DailyRevenueEntry;
import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyRevenueEntryRepository extends JpaRepository<DailyRevenueEntry, Long> {
	Optional<DailyRevenueEntry> findByMonthlyRecordAndDay(MonthlyRecord monthlyRecord, Integer day);

	List<DailyRevenueEntry> findByMonthlyRecordOrderByDayAsc(MonthlyRecord monthlyRecord);
}
