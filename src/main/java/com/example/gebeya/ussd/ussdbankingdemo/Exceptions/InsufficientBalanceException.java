package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException() {
        this("Your balance is insufficient to perform required task");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
