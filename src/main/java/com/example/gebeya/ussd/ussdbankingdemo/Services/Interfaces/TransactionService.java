package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    void depositTransaction(Account account, BigDecimal amount, String otp, LocalDateTime expiredate);

    void withdrawTransaction(Account account, BigDecimal amount, String otp, LocalDateTime expiredate);

    void merchantWithdrawTransaction(Account account, BigDecimal amount);

    void TransferTransaction(Account account, BigDecimal amount);

    Transaction getTransactionByRrn(int rrn);

    Transaction saveTransaction(Transaction transaction);

    void deleteTransaction(int rrn);

    List<Transaction> getByAccount(Account account_num);

    Slice<Transaction> getTopNTransactionsByAccount(Account account, int size);

    Transaction updateTransaction(int rrn, Transaction updatedTransaction);

    Transaction saveMerchantWithdrawTransaction(Transaction merchantTransaction);

    Transaction saveMerchantDepositTransaction(Transaction merchantTransaction);
}
