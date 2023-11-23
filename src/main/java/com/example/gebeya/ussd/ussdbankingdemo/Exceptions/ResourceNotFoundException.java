package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        this("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
