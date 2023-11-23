package com.example.gebeya.ussd.ussdbankingdemo.Exceptions.handler;

import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.ExceptionDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionDTO<?> handleNotFound(ResourceNotFoundException exception) {
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionDTO<?> handleNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        return new ExceptionDTO<>("Invalid request body", HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAirtimeRequestException.class)
    public ExceptionDTO<?> handleInsufficientBalance(InvalidAirtimeRequestException exception) {
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ExceptionDTO<?> handleInsufficientBalance(InsufficientBalanceException exception) {
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MobileBankingException.class)
    public ExceptionDTO<?> handleMobileBankingError(MobileBankingException exception) {
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AirtimeApiException.class)
    public ExceptionDTO<?> handleAirtimeApiException(AirtimeApiException exception) {
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}
