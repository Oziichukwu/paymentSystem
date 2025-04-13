package com.example.paymentprocessing.exceptions;

public class ParentNotFoundException extends PaymentProcessingException{

    public ParentNotFoundException() {
    }

    public ParentNotFoundException(String message) {
        super(message);
    }

    public ParentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParentNotFoundException(Throwable cause) {
        super(cause);
    }
}
