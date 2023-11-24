/*
 * Mobile Banking System
 * 
 * Author Alelign Ayana 
 * 
 */
package com.example.gebeya.ussd.ussdbankingdemo.Controller;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.handler.*;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.TransactionService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.CustomerService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.2/users/transactions")
public class TransactionController {
    @Autowired
    private  TransactionService transactionService;
    @Autowired
    private AccountService accountService;


    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) { 
            ErrorResponse errorResponse = new ErrorResponse("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{rrn}")
    public ResponseEntity<?> getTransactionByRrn(@PathVariable int rrn) {
        try {
            Transaction transaction = transactionService.getTransactionByRrn(rrn);
            if (transaction != null) {
                return ResponseEntity.ok(transaction);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Transaction not found with RRN: " + rrn);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            
            ErrorResponse errorResponse = new ErrorResponse("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

   @GetMapping("/account/{account}")
        public ResponseEntity<List<Transaction>> getAccountTranscation(
            @PathVariable Account account
                         ) {
       List<Transaction> transactions = transactionService.getByAccount(account);
       return new ResponseEntity<>(transactions, HttpStatus.OK);
   }
   
    @GetMapping("/{accounts}/short-statements/{sizes}")
    public ResponseEntity<List<Transaction>> getShortStatementsPageable(
            @PathVariable String accounts,
            @PathVariable int sizes
    ) {
        try {
            Account customerAccount=accountService.getAccountByNum(accounts);

            Slice<Transaction> transactions = transactionService.getTopNTransactionsByAccount(customerAccount, sizes);
            List<Transaction> content = transactions.getContent(); // Extracting the content
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{rrn}")
    public ResponseEntity<?> deleteTransaction(@PathVariable int rrn) {
        try {
            if(transactionService.getTransactionByRrn(rrn)!=null) {
                transactionService.deleteTransaction(rrn);
                return ResponseEntity.ok("Transaction with RRN " + rrn + " has been successfully deleted.");
            }else{
                ErrorResponse errorResponse = new ErrorResponse("Transaction not found with RRN: " + rrn);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (TransactionNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Transaction not found with RRN: " + rrn);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
          
            ErrorResponse errorResponse = new ErrorResponse("Unexpected error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}