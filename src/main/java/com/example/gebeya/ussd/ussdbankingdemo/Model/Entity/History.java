package com.example.gebeya.ussd.ussdbankingdemo.Model.Entity;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Represents a history entity for transactions.
 *
 * This class is an entity mapped to the database table representing transaction histories.
 * It contains information such as transaction details, status, and timestamps.
 *
 * @author Alazar Tilahun
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rrn;

    private String phoneNumber;
    private String transactionCode;
    private Status side;
    private BigDecimal amount;
    private String responseCode;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal startBalance;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "cif")
    @JsonBackReference
    private Customer customer;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "account_num")
    private Account account;
}
