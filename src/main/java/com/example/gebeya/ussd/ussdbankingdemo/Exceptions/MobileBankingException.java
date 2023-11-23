package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when the code is unable to process mobile user
 *
 * @author Dereje Desta
 */
public class MobileBankingException extends Throwable {
    public MobileBankingException() {
    }

    public MobileBankingException(String message) {
        super(message);
    }
}
