package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        this("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
