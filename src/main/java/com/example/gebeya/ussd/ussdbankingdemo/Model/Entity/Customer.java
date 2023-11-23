package com.example.gebeya.ussd.ussdbankingdemo.Model.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a customer entity.
 *
 * This class is an entity mapped to the database table representing bank customers.
 * It contains information such as customer identification, personal details, and associated accounts, histories, and mobile banking user.
 *
 * @author Alazar Tilahun
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cif;

    private String firstName;
    private String middleName;
    private String lastName;
    private String salutation;
    private String account_number;
    private String homeAddress;
    private String homePhone;
    private String email;
    private LocalDateTime dob;
    private String city;
    private String state;
    private String country;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Account> accounts;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<History> histories;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private MobileBankingUser mobileBankingUsers;

}
