package com.example.gebeya.ussd.ussdbankingdemo.Model.Entity;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a bank account entity.
 *
 * This class is an entity mapped to the database table representing bank accounts.
 * It contains information such as account number, balance, status, creation and update timestamps,
 * associated customer, transactions, and history.
 *
 * @author Alazar Tilahun
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Account {
    @Id

    private int AccountNum;

    private BigDecimal balance;
    private Status accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cif")
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transaction> transactions;


    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<History> history;
}
