package com.example.gebeya.ussd.ussdbankingdemo.Model.Entity;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Represents a transaction entity.
 *
 * This class is an entity mapped to the database table representing financial transactions.
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
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rrn;

    private String transactionCode;
    private Status side;
    private BigDecimal amount;
    private String respCode;
    private String OTP;
    private LocalDateTime otpexpireddate;
    private Status transactionStatus;
    private LocalDateTime transactionDate;
    private String customerAccount;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "account_num")// This should match the actual column name in the Account entity
    private Account account;
}
