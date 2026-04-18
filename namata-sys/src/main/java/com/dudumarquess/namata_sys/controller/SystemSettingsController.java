package com.dudumarquess.namata_sys.controller;

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
    public ResponseEntity<SystemSettingsResponse> getSettings() {
        return ResponseEntity.ok(systemSettingsService.getSettings());
    }

    @PutMapping
    public ResponseEntity<SystemSettingsResponse> update(
            @Valid @RequestBody UpdateSystemSettingsRequest request
    ) {
        return ResponseEntity.ok(systemSettingsService.update(request));
    }
}

