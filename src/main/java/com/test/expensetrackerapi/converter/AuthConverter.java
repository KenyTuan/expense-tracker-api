package com.test.expensetrackerapi.converter;

import com.test.expensetrackerapi.dtos.auth.AuthRes;
import com.test.expensetrackerapi.model.entity.User;

public class AuthConverter {

    /**
     * Converts a User and token to a AuthRes.
     *
     * @param token The token to convert.
     * @param user The User to convert.
     * @return The converted AuthRes.
     */
    public static AuthRes convertToDto(String token, User user) {
        return new AuthRes(
                token,
                UserConverter.convertToDto(user)
        );
    }
}
