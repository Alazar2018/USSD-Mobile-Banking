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
public class WithdrawDTO {
    private String accountNumber;
    private BigDecimal amount;
    private String customerAccountNumber;
    private String otp;
    private String password;

}
