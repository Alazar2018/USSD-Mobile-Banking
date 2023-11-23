package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.TransactionRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.HistoryService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.TransactionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
/**
 * Service class for managing transactions.
 *
 *
 * This service provides methods for handling various transaction operations, such as deposit,
 * withdrawal, transfer, and retrieval of transaction details. It also interacts with the
 * {@link TransactionRepository} and {@link HistoryService} for persistence and history tracking.
 *
 *
 *
 * @author Alazar Tilahun
 *
 *
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    private HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    @Override
    public void depositTransaction(Account account, BigDecimal amount, String otp, LocalDateTime expiredate) {
        // Implementation...
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setOTP(otp);
        transaction.setOtpexpireddate(expiredate);
        transaction.setSide(Status.DEPOSIT);
        transaction.setTransactionStatus(Status.PENDING);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRespCode("Deposit is Pending Please go to near by Merchant and Activate your deposit");
        //transaction.setStatus(Status.SUCCESSFUL);
        String generatedTransactionNumber = generateTransactionNumber();
        // Set the generated account number to the customer
        transaction.setTransactionCode(generatedTransactionNumber);
        // Set other transaction details like date, etc.

        // Save the transaction
        transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public void withdrawTransaction(Account account, BigDecimal amount, String otp, LocalDateTime expiredate) {
        // Implementation...
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setOTP(otp);
        transaction.setOtpexpireddate(expiredate);
        transaction.setSide(Status.WITHDRAW);
        transaction.setTransactionStatus(Status.PENDING);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRespCode("Withdraw is Pending Please go to near by Merchant and Activate your deposit");
        //transaction.setStatus(Status.SUCCESSFUL);
        String generatedTransactionNumber = generateTransactionNumber();
        // Set the generated account number to the customer
        transaction.setTransactionCode(generatedTransactionNumber);
        // Set other transaction details like date, etc.

        // Save the transaction
        transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public void merchantWithdrawTransaction(Account account, BigDecimal amount) {
        // Implementation...
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setOTP(null);
        transaction.setOtpexpireddate(null);
        transaction.setSide(Status.WITHDRAW);
        transaction.setTransactionStatus(Status.SUCCESSFUL);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRespCode("Withdraw is Successful");
        String generatedTransactionNumber = generateTransactionNumber();
        transaction.setTransactionCode(generatedTransactionNumber);
        historyService.saveHistoryForTransaction(transaction);
        transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public void TransferTransaction(Account account, BigDecimal amount) {
        // Implementation...
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setSide(Status.WITHDRAW);
        transaction.setTransactionStatus(Status.SUCCESSFUL);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRespCode("Transfer was succcessful");
        //transaction.setStatus(Status.SUCCESSFUL);
        String generatedTransactionNumber = generateTransactionNumber();
        // Set the generated account number to the customer
        transaction.setTransactionCode(generatedTransactionNumber);
        // Set other transaction details like date, etc.
        historyService.saveHistoryForTransaction(transaction);

        // Save the transaction
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionByRrn(int rrn) {
        return transactionRepository.findById(rrn).orElse(null);
    }

    @Transactional
    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public void deleteTransaction(int rrn) {
        transactionRepository.deleteById(rrn);
    }

    @Override
    public List<Transaction> getByAccount(Account account_num) {
        return transactionRepository.findByAccount(account_num);
    }

    @Override
    public Slice<Transaction> getTopNTransactionsByAccount(Account account, int size) {
        PageRequest pageable = PageRequest.of(0, size, Sort.by(Sort.Order.asc("transactionDate")));
        return transactionRepository.findTopNByAccountOrderByTransactionDateDesc(account, pageable);
    }

    @Transactional
    @Override
    public Transaction updateTransaction(int rrn, Transaction updatedTransaction) {
        // Implementation...
        // Detach the entity from the persistence context
        entityManager.detach(updatedTransaction);

        Transaction currentTransaction = transactionRepository.findById(rrn).orElse(null);

        if (currentTransaction != null) {
            currentTransaction.setAmount(updatedTransaction.getAmount());
            currentTransaction.setTransactionCode(updatedTransaction.getTransactionCode());
            currentTransaction.setAccount(updatedTransaction.getAccount());
            currentTransaction.setTransactionStatus(updatedTransaction.getTransactionStatus());
            currentTransaction.setSide(updatedTransaction.getSide());
            currentTransaction.setTransactionDate(updatedTransaction.getTransactionDate());
            currentTransaction.setOtpexpireddate(updatedTransaction.getOtpexpireddate());
            currentTransaction.setRespCode(updatedTransaction.getRespCode());
        }

        assert currentTransaction != null;
        return transactionRepository.save(currentTransaction);
    }

    @Transactional
    @Override
    public Transaction saveMerchantWithdrawTransaction(Transaction merchantTransaction) {
        // Implementation...
        merchantTransaction.setRespCode("Merchant Withdraw transaction");
        merchantTransaction.setTransactionDate(LocalDateTime.now());
        merchantTransaction.setSide(Status.WITHDRAW);
        merchantTransaction.setTransactionStatus(Status.SUCCESSFUL);
        merchantTransaction.setTransactionCode(generateTransactionNumber());
        historyService.saveHistoryForTransaction(merchantTransaction);

        return transactionRepository.save(merchantTransaction);
    }

    @Transactional
    @Override
    public Transaction saveMerchantDepositTransaction(Transaction merchantTransaction) {
        // Implementation...
        merchantTransaction.setRespCode("Merchant Deposit transaction");
        merchantTransaction.setTransactionDate(LocalDateTime.now());
        merchantTransaction.setSide(Status.DEPOSIT);
        merchantTransaction.setTransactionStatus(Status.SUCCESSFUL);
        merchantTransaction.setTransactionCode(generateTransactionNumber());
        historyService.saveHistoryForTransaction(merchantTransaction);

        return transactionRepository.save(merchantTransaction);
    }

    private String generateTransactionNumber() {
        // Implementation...
        // Use timestamp to ensure uniqueness
        String timestamp = LocalDateTime.now().toString().replace("-", "").replace(":", "").replace(".", "").replace("T", "").replace(" ", "");

        // Use a random component to enhance uniqueness
        Random random = new Random();
        int randomSuffix = random.nextInt(10000); // Adjust the range as needed

        // Combine timestamp and random component
        return timestamp + String.format("%04d", randomSuffix);
    }
}
