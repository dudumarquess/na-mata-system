package com.dudumarquess.namata_sys.repository;

import com.dudumarquess.namata_sys.entity.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {
	Optional<SystemSettings> findFirstByOrderByIdAsc();
}
