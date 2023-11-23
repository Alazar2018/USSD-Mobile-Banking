package com.example.gebeya.ussd.ussdbankingdemo.Exceptions.handler;

import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.ExceptionDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations.HistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * handle exceptions thrown by controllers
 *
 * @author Dereje Desta
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionDTO<?> handleNotFound(ResourceNotFoundException exception) {
        log.info("resource not found: {}", exception.getMessage());
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionDTO<?> handleNotValid(MethodArgumentNotValidException exception) {
        log.info("invalid body: {}", exception.getMessage());
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        log.info("invalid body : {}", errors);
        return new ExceptionDTO<>("Invalid request body", HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAirtimeRequestException.class)
    public ExceptionDTO<?> handleInsufficientBalance(InvalidAirtimeRequestException exception) {
        log.info("invalid airtime request: {}", exception.getMessage());
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientBalanceException.class)
    public ExceptionDTO<?> handleInsufficientBalance(InsufficientBalanceException exception) {
        log.info("insufficient balance: {}", exception.getMessage());
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MobileBankingException.class)
    public ExceptionDTO<?> handleMobileBankingError(MobileBankingException exception) {
        log.info("mobile banking error: {}", exception.getMessage());
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AirtimeApiException.class)
    public ExceptionDTO<?> handleAirtimeApiException(AirtimeApiException exception) {
        log.info("airtime api exception: {}", exception.getMessage());
        return new ExceptionDTO<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}
