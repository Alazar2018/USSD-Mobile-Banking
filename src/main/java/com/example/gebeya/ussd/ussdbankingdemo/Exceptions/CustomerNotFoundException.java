package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when customer can't be found
 *
 * @author Dereje Desta
 */
public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException() {
        this("Customer is not found");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
