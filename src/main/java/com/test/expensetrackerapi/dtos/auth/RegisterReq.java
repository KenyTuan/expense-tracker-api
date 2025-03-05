package com.test.expensetrackerapi.dtos.auth;

import com.test.expensetrackerapi.constants.MessageException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RegisterReq implements Serializable {
    @NotBlank(message = MessageException.NAME_INVALID)
    private String name;

    @Pattern(message = MessageException.EMAIL_INVALID,
            regexp = "^(?!.*@.*@)(?!.*\\.\\.)(?!.*@\\.)(?!.*\\.$)(?!^\\.)(?!.*\\s)[^@]+@[^@]+\\.[^@]+$")
    private String email;

    @Pattern(message = MessageException.PASS_INVALID,
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;
}
