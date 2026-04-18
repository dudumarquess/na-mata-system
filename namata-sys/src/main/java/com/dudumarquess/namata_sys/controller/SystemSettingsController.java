package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.api.ApiResponse;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.UpdateSystemSettingsRequest;
import com.dudumarquess.namata_sys.dto.response.SystemSettingsResponse;
import com.dudumarquess.namata_sys.service.SystemSettingsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system-settings")
public class SystemSettingsController {

    private final SystemSettingsService systemSettingsService;

    public SystemSettingsController(SystemSettingsService systemSettingsService) {
        this.systemSettingsService = systemSettingsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SystemSettingsResponse>> getSettings() {
        return toResponse(systemSettingsService.getSettings());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<SystemSettingsResponse>> update(
            @Valid @RequestBody UpdateSystemSettingsRequest request
    ) {
        return toResponse(systemSettingsService.update(request));
    }

    private <T> ResponseEntity<ApiResponse<T>> toResponse(ServiceResult<T> result) {
        ApiResponse<T> response = result.success()
                ? ApiResponse.success(result.message(), result.data())
                : ApiResponse.failure(result.message(), result.errors());
        return ResponseEntity.status(result.statusCode()).body(response);
    }
}

