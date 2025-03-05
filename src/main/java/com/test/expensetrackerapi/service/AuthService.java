package com.test.expensetrackerapi.service;

import com.test.expensetrackerapi.dtos.auth.AuthRes;
import com.test.expensetrackerapi.dtos.auth.LoginReq;
import com.test.expensetrackerapi.dtos.auth.RegisterReq;
import com.test.expensetrackerapi.dtos.user.UserRes;

public interface AuthService {
    UserRes register(RegisterReq req);

    AuthRes login(LoginReq req);
}
