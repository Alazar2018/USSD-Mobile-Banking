package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when error happen while processing airtime request
 *
 * @author Dereje Desta
 */
public class AirtimeApiException extends Throwable {
    public AirtimeApiException() {
        this("unable to buy airtime");
    }

    public AirtimeApiException(String message) {
        super(message);
    }
}
