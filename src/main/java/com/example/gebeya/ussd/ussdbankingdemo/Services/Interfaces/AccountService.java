package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccountByNum(String accountNum) throws AccountNotFoundException;
    List<Account> getAllAccountsOfCustomer(Customer customer);
    Account saveAccount(Account account);
    Account saveAccountForCustomer(int cif, Account account) throws CustomerNotFoundException;
    String depositMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException, MobileBankingUserNotFoundException, InsufficientBalanceException;
    String verifyDeposit(String accountNumber, String customerAccountNumber, String OTP) throws AccountNotFoundException, TransactionNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String verifyWithdraw(String accountNumber, String customerAccountNumber, String OTP) throws AccountNotFoundException, TransactionNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String withdrawMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String transactionMoney(String senderAccountNumber, String recieverAccountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException;
    String updateAccount(Account account);
    boolean deleteAccount(int accountNum);
    boolean checkMerchantUser(Account account) throws MobileBankingUserNotFoundException;
    boolean checkCustomerUser(Account account) throws MobileBankingUserNotFoundException;

}
