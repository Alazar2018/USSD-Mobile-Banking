package com.example.gebeya.ussd.ussdbankingdemo.Model.DTO;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AirtimeTopUpDTO {
    private String account;
    private BigDecimal amount;
}
