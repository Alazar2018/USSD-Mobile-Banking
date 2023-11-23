package com.example.gebeya.ussd.ussdbankingdemo.Model.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {
    private int accountNumber;
    private BigDecimal amount;
    private int customerAccountNumber;
    private String otp;
    private String password;


    // Constructors, getters, and setters
    // You can generate these using your IDE or manually implement them
}
