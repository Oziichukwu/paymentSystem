package com.example.paymentprocessing.data.dtos.ResponseDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
public class ApiResponse {

    private boolean isSuccessful;

    private String message;

    private LocalDateTime responseDate;

    public ApiResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        responseDate = LocalDateTime.now();
    }

}
