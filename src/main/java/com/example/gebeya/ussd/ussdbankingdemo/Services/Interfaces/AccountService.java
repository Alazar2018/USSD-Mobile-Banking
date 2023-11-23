package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccountByNum(int accountNum) throws AccountNotFoundException;
    List<Account> getAllAccountsOfCustomer(Customer customer);
    Account saveAccount(Account account);
    Account saveAccountForCustomer(int cif, Account account) throws CustomerNotFoundException;
    String depositMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException, MobileBankingUserNotFoundException;
    String verifyDeposit(int accountNumber, int customerAccountNumber, String OTP) throws AccountNotFoundException, TransactionNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String verifyWithdraw(int accountNumber, int customerAccountNumber, String OTP) throws AccountNotFoundException, TransactionNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String withdrawMoney(int accountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String transactionMoney(String senderAccountNumber, String recieverAccountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String updateAccount(Account account);
    boolean deleteAccount(int accountNum);
}
