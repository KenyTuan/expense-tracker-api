package com.test.expensetrackerapi.exception;

public class BadRequestException extends CustomException {
    public BadRequestException(String errCode, String msg) {
        super(errCode, msg);
    }
}
