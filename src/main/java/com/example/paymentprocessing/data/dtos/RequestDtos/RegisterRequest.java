package com.example.paymentprocessing.data.dtos.RequestDtos;


import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private Set<String> roles;
}
