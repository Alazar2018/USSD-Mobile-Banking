package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException() {
        this("Customer is not found");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
