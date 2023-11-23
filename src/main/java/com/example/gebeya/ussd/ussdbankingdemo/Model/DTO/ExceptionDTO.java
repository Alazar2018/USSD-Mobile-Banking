package com.example.gebeya.ussd.ussdbankingdemo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExceptionDTO<T> {
    private String message;
    private HttpStatus statusCode;
    private T details;
}
