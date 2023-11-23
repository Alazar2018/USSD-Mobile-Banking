package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when users airtime request is invalid
 *
 * @author Dereje Desta
 */
public class InvalidAirtimeRequestException extends Throwable{
    public InvalidAirtimeRequestException() {
        this("invalid airtime request body");
    }

    public InvalidAirtimeRequestException(String message) {
        super(message);
    }
}
