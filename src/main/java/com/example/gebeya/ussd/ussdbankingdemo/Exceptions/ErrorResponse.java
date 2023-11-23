package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

public class ErrorResponse extends RuntimeException{
    public ErrorResponse(String message){
        super(message);
    }
}
