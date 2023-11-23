package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when mobile user can't be found
 *
 * @author Dereje Desta
 */
public class MobileBankingUserNotFoundException extends ResourceNotFoundException {
    public MobileBankingUserNotFoundException() {
        this("Mobile user not found");
    }

    public MobileBankingUserNotFoundException(String message) {
        super(message);
    }
}
