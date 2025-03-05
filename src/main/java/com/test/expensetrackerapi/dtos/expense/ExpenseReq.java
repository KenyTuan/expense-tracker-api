package com.test.expensetrackerapi.dtos.expense;

import com.test.expensetrackerapi.constants.MessageException;
import com.test.expensetrackerapi.model.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class ExpenseReq implements Serializable {
    @NotBlank(message = MessageException.DESCRIPTION_INVALID)
    private String description;

    @Min(value = 0, message = MessageException.AMOUNT_INVALID)
    private double amount;

    @NotBlank(message = MessageException.USER_ID_INVALID)
    private String userId;

    @NotNull(message = MessageException.CATEGORY_NAME_INVALID)
    private Category category;
}
