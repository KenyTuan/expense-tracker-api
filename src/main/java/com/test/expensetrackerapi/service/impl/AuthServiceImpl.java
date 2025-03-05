package com.test.expensetrackerapi.service.impl;

import com.test.expensetrackerapi.config.security.JwtUtil;
import com.test.expensetrackerapi.converter.AuthConverter;
import com.test.expensetrackerapi.converter.UserConverter;
import com.test.expensetrackerapi.dtos.auth.AuthRes;
import com.test.expensetrackerapi.dtos.auth.LoginReq;
import com.test.expensetrackerapi.dtos.auth.RegisterReq;
import com.test.expensetrackerapi.dtos.user.UserRes;
import com.test.expensetrackerapi.exception.BadRequestException;
import com.test.expensetrackerapi.exception.ErrorCode;
import com.test.expensetrackerapi.exception.NotFoundException;
import com.test.expensetrackerapi.model.entity.User;
import com.test.expensetrackerapi.repository.UserRepository;
import com.test.expensetrackerapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager  authenticationManager;

    @Override
    public UserRes register(RegisterReq req) {
        final boolean exists = userRepository
                .existsByEmail(req.getEmail());

        if(exists){
            throw new NotFoundException(ErrorCode.EMAIL_ALREADY_EXISTS.getErrMessage());
        }

        final User user = UserConverter.convertToEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return UserConverter.convertToDto(userRepository.save(user));
    }

    @Override
    public AuthRes login(LoginReq req) {
        final User user = findUserByEmail(req.getEmail());

        checkMatchesPassword(user, req.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword())
        );

        return AuthConverter.convertToDto(
                jwtUtil.generateToken(user),user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException(
                                ErrorCode.USER_NOT_FOUND.getErrMessage())
                );
    }

    private void checkMatchesPassword(User user, String password) {
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BadRequestException(
                    ErrorCode.PASSWORD_INCORRECT.getErrCode(),
                    ErrorCode.PASSWORD_INCORRECT.getErrMessage()
            );
        }
    }
}
