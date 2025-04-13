package com.example.paymentprocessing.exceptions;

public class PaymentProcessingException extends RuntimeException{

    public PaymentProcessingException() {
    }

    public PaymentProcessingException(String message) {
        super(message);
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentProcessingException(Throwable cause) {
        super(cause);
    }
}
