package com.example.gebeya.ussd.ussdbankingdemo.exceptions;

public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException() {
        this("Your balance is insufficient to perform required task");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
