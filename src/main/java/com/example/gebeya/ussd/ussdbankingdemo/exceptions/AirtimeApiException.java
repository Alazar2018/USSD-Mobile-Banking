package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class AirtimeApiException extends Throwable {
    public AirtimeApiException() {
        this("unable to buy airtime");
    }

    public AirtimeApiException(String message) {
        super(message);
    }
}
