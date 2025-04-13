package com.example.paymentprocessing.data.dtos.RequestDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull(message = "parentId field cannot be null")
    private Long parentId;

    @NotNull(message = "studentId field cannot be null")
    private Long studentId;

    @NotNull(message = "paymentAmount field cannot be null")
    private BigDecimal paymentAmount;
}
