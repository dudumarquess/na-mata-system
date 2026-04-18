package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.FixedExpenseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixedExpenseTemplateRepository extends JpaRepository<FixedExpenseTemplate, Long> {
	List<FixedExpenseTemplate> findByActiveTrueAndAutoLaunchEnabledTrue();
}
