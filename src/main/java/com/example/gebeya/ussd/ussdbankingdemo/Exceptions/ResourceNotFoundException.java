package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when resource can't be found
 *
 * It is inheritable for other specific resources to be thrown
 *
 * @author Dereje Desta
 */
public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        this("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
