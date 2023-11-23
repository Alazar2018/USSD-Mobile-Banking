package com.example.gebeya.ussd.ussdbankingdemo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AirtimeTopUpResponseDTO {
    private String senum;
    private String scnum;
    private String expDate;
}
