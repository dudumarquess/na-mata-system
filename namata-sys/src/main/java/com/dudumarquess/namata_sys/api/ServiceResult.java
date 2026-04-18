package com.dudumarquess.namata_sys.api;

import java.util.Collections;
import java.util.List;

public record ServiceResult<T>(
        boolean success,
        String message,
        T data,
        List<ApiFieldError> errors,
        int statusCode
) {
    public ServiceResult {
        errors = errors == null ? Collections.emptyList() : errors;
    }

    public static <T> ServiceResult<T> success(String message, T data, int statusCode) {
        return new ServiceResult<>(true, message, data, Collections.emptyList(), statusCode);
    }

    public static <T> ServiceResult<T> failure(String message, List<ApiFieldError> errors, int statusCode) {
        return new ServiceResult<>(false, message, null, errors, statusCode);
    }

    public static <T> ServiceResult<T> failure(String message, int statusCode) {
        return new ServiceResult<>(false, message, null, Collections.emptyList(), statusCode);
    }
}

