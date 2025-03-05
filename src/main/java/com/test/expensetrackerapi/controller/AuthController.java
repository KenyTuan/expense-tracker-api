package com.test.expensetrackerapi.controller;

import com.test.expensetrackerapi.constants.APIEndPoint;
import com.test.expensetrackerapi.dtos.auth.AuthRes;
import com.test.expensetrackerapi.dtos.auth.LoginReq;
import com.test.expensetrackerapi.dtos.auth.RegisterReq;
import com.test.expensetrackerapi.dtos.user.UserRes;
import com.test.expensetrackerapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIEndPoint.PREFIX)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(APIEndPoint.AUTH_V1 + "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRes register(@RequestBody @Valid RegisterReq req) {
        return authService.register(req);
    }

    @PostMapping(APIEndPoint.AUTH_V1 + "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthRes login(@RequestBody @Valid LoginReq loginReq) {
        return authService.login(loginReq);
    }
}
