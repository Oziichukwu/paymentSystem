package com.example.paymentprocessing.service;

import com.example.paymentprocessing.data.dtos.RequestDtos.PaymentRequest;

public interface PaymentService {

    void processPayment(PaymentRequest paymentRequest);

}
