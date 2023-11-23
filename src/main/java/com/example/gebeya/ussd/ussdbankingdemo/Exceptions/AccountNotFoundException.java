package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * it is used when the account couldn't be found
 *
 * @author Dereje Desta
 */
public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException() {
        this("Account not found");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
