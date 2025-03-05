package com.test.expensetrackerapi.model.entity;

import com.test.expensetrackerapi.model.enums.Category;
import com.test.expensetrackerapi.model.enums.ObjStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense implements Serializable {
    @Id
    private String id;

    private String userId;

    private String description;

    private double amount;

    private Category category;

    private LocalDateTime dateTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    private ObjStatus objStatus;
}
