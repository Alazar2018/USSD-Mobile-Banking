package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class InvalidAirtimeRequestException extends Throwable{
    public InvalidAirtimeRequestException() {
        this("invalid airtime request body");
    }

    public InvalidAirtimeRequestException(String message) {
        super(message);
    }
}
