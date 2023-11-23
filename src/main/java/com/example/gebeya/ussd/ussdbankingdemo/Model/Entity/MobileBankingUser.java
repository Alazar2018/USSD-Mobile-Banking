package com.example.gebeya.ussd.ussdbankingdemo.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
/**
 * Represents a mobile banking user entity.
 *
 * This class is an entity mapped to the database table representing mobile banking users.
 * It contains information such as user details, authentication credentials, and associated customer.
 *
 * @author Alazar Tilahun
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class MobileBankingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mobileBankingUserId;

    private String customerProfile;
    private String userName;
    private String password;
    private int version;
    private String language;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "cif")
    private Customer customer;
}
