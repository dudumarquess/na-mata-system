package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.api.ApiFieldError;
import com.dudumarquess.namata_sys.api.ServiceResult;
import com.dudumarquess.namata_sys.dto.request.CreateFixedExpenseTemplateRequest;
import com.dudumarquess.namata_sys.dto.request.UpdateFixedExpenseTemplateRequest;
import com.dudumarquess.namata_sys.dto.response.FixedExpenseTemplateListResponse;
import com.dudumarquess.namata_sys.dto.response.FixedExpenseTemplateResponse;
import com.dudumarquess.namata_sys.entity.FixedExpenseTemplate;
import com.dudumarquess.namata_sys.repository.FixedExpenseTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FixedExpenseTemplateService {

	private final FixedExpenseTemplateRepository fixedExpenseTemplateRepository;

	public FixedExpenseTemplateService(FixedExpenseTemplateRepository fixedExpenseTemplateRepository) {
		this.fixedExpenseTemplateRepository = fixedExpenseTemplateRepository;
	}

	@Transactional
	public ServiceResult<FixedExpenseTemplateResponse> create(CreateFixedExpenseTemplateRequest request) {
		List<ApiFieldError> errors = validateRequest(
				request == null ? null : request.name(),
				request == null ? null : request.defaultAmount(),
				request == null ? null : request.defaultDay(),
				request == null ? null : request.active(),
				request == null ? null : request.autoLaunchEnabled()
		);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid fixed expense template payload", errors, 400);
		}

		FixedExpenseTemplate template = new FixedExpenseTemplate();
		template.setName(request.name().trim());
		template.setDefaultAmount(request.defaultAmount());
		template.setDefaultDay(request.defaultDay());
		template.setActive(request.active());
		template.setAutoLaunchEnabled(request.autoLaunchEnabled());

		FixedExpenseTemplate saved = fixedExpenseTemplateRepository.save(template);
		return ServiceResult.success("Fixed expense template created successfully", toResponse(saved), 201);
	}

	@Transactional
	public ServiceResult<FixedExpenseTemplateResponse> update(Long id, UpdateFixedExpenseTemplateRequest request) {
		if (id == null) {
			return ServiceResult.failure("Invalid template id", List.of(new ApiFieldError("id", "Id is required")), 400);
		}

		List<ApiFieldError> errors = validateRequest(
				request == null ? null : request.name(),
				request == null ? null : request.defaultAmount(),
				request == null ? null : request.defaultDay(),
				request == null ? null : request.active(),
				request == null ? null : request.autoLaunchEnabled()
		);
		if (!errors.isEmpty()) {
			return ServiceResult.failure("Invalid fixed expense template payload", errors, 400);
		}

		FixedExpenseTemplate template = fixedExpenseTemplateRepository.findById(id).orElse(null);
		if (template == null) {
			return ServiceResult.failure("Fixed expense template not found", 404);
		}

		template.setName(request.name().trim());
		template.setDefaultAmount(request.defaultAmount());
		template.setDefaultDay(request.defaultDay());
		template.setActive(request.active());
		template.setAutoLaunchEnabled(request.autoLaunchEnabled());

		FixedExpenseTemplate saved = fixedExpenseTemplateRepository.save(template);
		return ServiceResult.success("Fixed expense template updated successfully", toResponse(saved), 200);
	}

	@Transactional(readOnly = true)
	public ServiceResult<FixedExpenseTemplateListResponse> getAll() {
		List<FixedExpenseTemplateResponse> templates = fixedExpenseTemplateRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(FixedExpenseTemplate::getName, String.CASE_INSENSITIVE_ORDER))
				.map(this::toResponse)
				.toList();

		return ServiceResult.success(
				"Fixed expense templates loaded successfully",
				new FixedExpenseTemplateListResponse(templates),
				200
		);
	}

	private List<ApiFieldError> validateRequest(
			String name,
			BigDecimal defaultAmount,
			Integer defaultDay,
			Boolean active,
			Boolean autoLaunchEnabled
	) {
		List<ApiFieldError> errors = new ArrayList<>();
		if (name == null || name.isBlank()) {
			errors.add(new ApiFieldError("name", "Name is required"));
		}
		if (defaultAmount == null || defaultAmount.compareTo(BigDecimal.ZERO) < 0) {
			errors.add(new ApiFieldError("defaultAmount", "Default amount must be greater than or equal to 0.00"));
		}
		if (defaultDay == null || defaultDay < 1 || defaultDay > 31) {
			errors.add(new ApiFieldError("defaultDay", "Default day must be between 1 and 31"));
		}
		if (active == null) {
			errors.add(new ApiFieldError("active", "Active flag is required"));
		}
		if (autoLaunchEnabled == null) {
			errors.add(new ApiFieldError("autoLaunchEnabled", "Auto launch flag is required"));
		}
		return errors;
	}

	private FixedExpenseTemplateResponse toResponse(FixedExpenseTemplate template) {
		return new FixedExpenseTemplateResponse(
				template.getId(),
				template.getName(),
				template.getDefaultAmount(),
				template.getDefaultDay(),
				template.getActive(),
				template.getAutoLaunchEnabled()
		);
	}
}
