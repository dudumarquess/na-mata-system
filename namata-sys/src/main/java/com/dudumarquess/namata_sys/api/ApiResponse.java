package com.dudumarquess.namata_sys.api;

import java.util.Collections;
import java.util.List;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        List<ApiFieldError> errors
) {
    public ApiResponse {
        errors = errors == null ? Collections.emptyList() : errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, Collections.emptyList());
    }

    public static <T> ApiResponse<T> failure(String message, List<ApiFieldError> errors) {
        return new ApiResponse<>(false, message, null, errors);
    }
}

