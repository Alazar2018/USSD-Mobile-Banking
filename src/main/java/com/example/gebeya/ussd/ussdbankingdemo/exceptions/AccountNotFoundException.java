package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException() {
        this("Account not found");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
