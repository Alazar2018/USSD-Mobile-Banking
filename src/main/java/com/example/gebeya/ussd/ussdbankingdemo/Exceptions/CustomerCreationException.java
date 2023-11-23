package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

public class CustomerCreationException extends RuntimeException {
    public  CustomerCreationException (String message){
        super(message);
    }
}
