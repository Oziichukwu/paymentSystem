package com.example.paymentprocessing.web.controllers;


import com.example.paymentprocessing.data.dtos.RequestDtos.PaymentRequest;
import com.example.paymentprocessing.data.dtos.ResponseDtos.ApiResponse;
import com.example.paymentprocessing.exceptions.InsufficientBalanceException;
import com.example.paymentprocessing.exceptions.ParentNotFoundException;
import com.example.paymentprocessing.exceptions.StudentNotFoundException;
import com.example.paymentprocessing.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            paymentService.processPayment(paymentRequest);
            return new ResponseEntity<>(new ApiResponse(true, "Payment processed successfully"), HttpStatus.OK);
        }catch (InsufficientBalanceException | ParentNotFoundException |
                StudentNotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
