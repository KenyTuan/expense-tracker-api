package com.test.expensetrackerapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    public String errCode;

    public String errMsg;
}
