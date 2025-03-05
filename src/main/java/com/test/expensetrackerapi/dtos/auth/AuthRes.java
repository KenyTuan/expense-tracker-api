package com.test.expensetrackerapi.dtos.auth;

import com.test.expensetrackerapi.dtos.user.UserRes;

import java.io.Serializable;

public record AuthRes(
        String token,
        UserRes user
) implements Serializable {
}
