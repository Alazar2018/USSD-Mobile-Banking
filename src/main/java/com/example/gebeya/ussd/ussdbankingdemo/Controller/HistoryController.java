/*
 * Mobile Banking System
 * 
 * Author Alelign Ayana 
 * 
 */
package com.example.gebeya.ussd.ussdbankingdemo.Controller;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.CustomerUpdateDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.DepositDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.TransferDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.WithdrawDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.History;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.CustomerService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.HistoryService;

import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1.2/histories")

public class HistoryController {
    @Autowired
    private  HistoryService historyService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MobileBankingUserService mobileBankingUserService;

    @GetMapping
    public ResponseEntity<?> getAllHistories() {
        try {
            List<History> histories = historyService.getAllHistories();

            if (histories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No history entries found");
            } else {
                return ResponseEntity.ok(histories);
            }

        } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

}

