package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class MobileBankingUserNotFoundException extends ResourceNotFoundException {
    public MobileBankingUserNotFoundException() {
        this("Mobile user not found");
    }

    public MobileBankingUserNotFoundException(String message) {
        super(message);
    }
}
