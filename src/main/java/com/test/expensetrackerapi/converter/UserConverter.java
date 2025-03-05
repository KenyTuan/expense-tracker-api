package com.test.expensetrackerapi.converter;

import com.test.expensetrackerapi.dtos.auth.RegisterReq;
import com.test.expensetrackerapi.dtos.user.UserRes;
import com.test.expensetrackerapi.model.entity.User;
import com.test.expensetrackerapi.model.enums.ObjStatus;
import com.test.expensetrackerapi.model.enums.Role;


public class UserConverter {

    /**
     * Converts a User to a UserRes.
     *
     * @param user The User to convert.
     * @return The converted UserRes.
     */
    public static UserRes convertToDto(User user) {
        return new UserRes(
                user.getId(),
                user.getEmail(),
                user.getName(),
                ExpenseConverter.convertToDtoList(user.getExpenses()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Converts a RegisterReq to a User.
     *
     * @param req The RegisterReq to convert.
     * @return The converted User.
     */
    public static User convertToEntity(RegisterReq req) {
        final User user = User
                .builder()
                .email(req.getEmail())
                .name(req.getName())
                .role(Role.USER)
                .build();
        user.setObjStatus(ObjStatus.ACTIVE);
        return user;
    }
}
