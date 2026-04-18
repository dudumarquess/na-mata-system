package com.dudumarquess.namata_sys.controller;

import com.dudumarquess.namata_sys.api.ApiResponse;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.OpenMonthlyRecordRequest;
import com.dudumarquess.namata_sys.dto.response.MonthlyRecordResponse;
import com.dudumarquess.namata_sys.service.MonthlyRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monthly-records")
public class MonthlyRecordController {

	private final MonthlyRecordService monthlyRecordService;

	public MonthlyRecordController(MonthlyRecordService monthlyRecordService) {
		this.monthlyRecordService = monthlyRecordService;
	}

	@PostMapping("/open")
	public ResponseEntity<ApiResponse<MonthlyRecordResponse>> open(@Valid @RequestBody OpenMonthlyRecordRequest request) {
		return toResponse(monthlyRecordService.openOrCreateMonth(request));
	}

	private <T> ResponseEntity<ApiResponse<T>> toResponse(ServiceResult<T> result) {
		ApiResponse<T> response = result.success()
				? ApiResponse.success(result.message(), result.data())
				: ApiResponse.failure(result.message(), result.errors());
		return ResponseEntity.status(result.statusCode()).body(response);
	}

}
