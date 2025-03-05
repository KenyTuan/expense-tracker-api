package com.test.expensetrackerapi.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ExceptionRes(
        String code,
        String message,
        Integer status,
        String url,
        String reqMethod,
        LocalDateTime timestamp
) implements Serializable {
}
