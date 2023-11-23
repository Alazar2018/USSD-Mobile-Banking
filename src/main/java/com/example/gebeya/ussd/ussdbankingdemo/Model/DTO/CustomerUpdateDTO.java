package com.example.gebeya.ussd.ussdbankingdemo.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerUpdateDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String salutation;
    private String homeAddress;
    private String homePhone;
    private String email;
    private LocalDateTime dob;
    private String city;
    private String state;
    private String country;

}
