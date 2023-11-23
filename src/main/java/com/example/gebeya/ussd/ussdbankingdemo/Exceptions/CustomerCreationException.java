package com.example.gebeya.ussd.ussdbankingdemo.Exceptions;

/**
 * thrown when customer creation is failed
 *
 * @author Dereje Desta
 */
public class CustomerCreationException extends RuntimeException {
    public  CustomerCreationException (String message){
        super(message);
    }
}
