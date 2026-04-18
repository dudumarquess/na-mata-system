package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.UpdateSystemSettingsRequest;
import com.dudumarquess.namata_sys.dto.response.SystemSettingsResponse;
import com.dudumarquess.namata_sys.entity.SystemSettings;
import com.dudumarquess.namata_sys.repository.SystemSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemSettingsService {

	private final SystemSettingsRepository systemSettingsRepository;

	public SystemSettingsService(SystemSettingsRepository systemSettingsRepository) {
		this.systemSettingsRepository = systemSettingsRepository;
	}

	@Transactional
	public ServiceResult<SystemSettingsResponse> getSettings() {
		SystemSettings settings = getOrCreateSettings();
		return ServiceResult.success("System settings loaded successfully", toResponse(settings), 200);
	}

	@Transactional
	public ServiceResult<SystemSettingsResponse> update(UpdateSystemSettingsRequest request) {
		List<ApiFieldError> errors = validateRequest(request);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid system settings payload", errors, 400);
		}

		SystemSettings settings = getOrCreateSettings();
		settings.setDefaultAppFeePercentage(request.defaultAppFeePercentage());

		SystemSettings saved = systemSettingsRepository.save(settings);
		return ServiceResult.success("System settings updated successfully", toResponse(saved), 200);
	}

	private SystemSettings getOrCreateSettings() {
		return systemSettingsRepository.findFirstByOrderByIdAsc().orElseGet(() -> {
			SystemSettings settings = new SystemSettings();
			settings.setDefaultAppFeePercentage(BigDecimal.ZERO);
			return systemSettingsRepository.save(settings);
		});
	}

	private List<ApiFieldError> validateRequest(UpdateSystemSettingsRequest request) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (request == null) {
			errors.add(new ApiFieldError("request", "Request body is required"));
			return errors;
		}
		if (request.defaultAppFeePercentage() == null) {
			errors.add(new ApiFieldError("defaultAppFeePercentage", "Default app fee percentage is required"));
		} else if (request.defaultAppFeePercentage().compareTo(BigDecimal.ZERO) < 0
				|| request.defaultAppFeePercentage().compareTo(BigDecimal.valueOf(100)) > 0) {
			errors.add(new ApiFieldError("defaultAppFeePercentage", "Default app fee percentage must be between 0.00 and 100.00"));
		}
		return errors;
	}

	private SystemSettingsResponse toResponse(SystemSettings settings) {
		return new SystemSettingsResponse(settings.getId(), settings.getDefaultAppFeePercentage());
	}
}
