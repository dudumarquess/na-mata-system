package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.MonthlyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyRecordRepository extends JpaRepository<MonthlyRecord, Long> {
	Optional<MonthlyRecord> findByYearAndMonth(Integer year, Integer month);
}
