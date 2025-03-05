package com.test.expensetrackerapi.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.expensetrackerapi.exception.ErrorCode;
import com.test.expensetrackerapi.exception.ExceptionRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("Unauthorized access attempt to {}: {}", request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ExceptionRes exceptionRes = new ExceptionRes(
                ErrorCode.UNAUTHORIZED.getErrCode(),
                ErrorCode.UNAUTHORIZED.getErrMessage() + ": " + authException.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURL().toString(),
                request.getMethod(),
                LocalDateTime.now()
        );

        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), exceptionRes);
    }
}
