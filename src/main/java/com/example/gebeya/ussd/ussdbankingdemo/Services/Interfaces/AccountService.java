package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.TransactionNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccountByNum(int accountNum);
    List<Account> getAllAccountsOfCustomer(Customer customer);
    Account saveAccount(Account account);
    Account saveAccountForCustomer(int cif, Account account);
    String depositMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException;
    String verifyDeposit(int accountNumber, int customerAccountNumber, String OTP) throws AccountNotFoundException, com.example.gebeya.ussd.ussdbankingdemo.Exceptions.InsufficientBalanceException, TransactionNotFoundException;
    String verifyWithdraw(int accountNumber, int customerAccountNumber, String OTP) throws AccountNotFoundException, TransactionNotFoundException, com.example.gebeya.ussd.ussdbankingdemo.Exceptions.InsufficientBalanceException;
    String withdrawMoney(int accountNumber, BigDecimal amount) throws AccountNotFoundException, com.example.gebeya.ussd.ussdbankingdemo.Exceptions.InsufficientBalanceException;
    String transactionMoney(String senderAccountNumber, String recieverAccountNumber, BigDecimal amount) throws AccountNotFoundException, com.example.gebeya.ussd.ussdbankingdemo.Exceptions.InsufficientBalanceException;
    String updateAccount(Account account);
    boolean deleteAccount(int accountNum);
}
