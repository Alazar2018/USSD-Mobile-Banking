package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.History;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.HistoryRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.HistoryService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Service class for managing history-related operations.
 *
 * This class provides methods for retrieving, saving, and deleting history records.
 * It also includes methods for creating and saving history records related to customer creation,
 * transaction history, customer updates, and account updates.
 *
 * @author Alazar Tilahun
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;
    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    @Override
    public List<History> getAllHistories() {
        log.info("getting all histories");
        return historyRepository.findAll();
    }

    @Override
    public History getHistoryByRrn(int rrn) {
        log.info("getting history by rrn {}", rrn);
        return historyRepository.findById(rrn).orElse(null);
    }

    @Transactional
    @Override
    public History saveHistory(History history) {
        log.info("saving history {}", history.getRrn());
        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public void deleteHistory(int rrn) {
        log.info("deleting history {}", rrn);
        historyRepository.deleteById(rrn);
    }

    @Override
    public History saveHistoryForCustomerCreated(Customer customer) {
        log.info("save history for customer {} created", customer.getCif());
        History history = new History();
        history.setCustomer(customer);
        history.setAccount((Account) customer.getAccounts());
        history.setCreatedAt(LocalDateTime.now());
        history.setBalance(((Account) customer.getAccounts()).getBalance());
        history.setStartBalance(((Account) customer.getAccounts()).getBalance());
        history.setPhoneNumber(customer.getHomePhone());
        return historyRepository.save(history);
    }

    @Override
    public History saveHistoryForTransaction(Transaction transaction) {
        log.info("saving history for transaction {}", transaction.getRrn());
        History history = new History();
        history.setSide(transaction.getSide());
        history.setAmount(transaction.getAmount());
        if (transaction.getSide() == Status.DEPOSIT) {
            BigDecimal startBalance = transaction.getAccount().getBalance().add(transaction.getAmount());
            history.setStartBalance(startBalance);
        } else {
            BigDecimal startBalance = transaction.getAccount().getBalance().subtract(transaction.getAmount());
            history.setStartBalance(startBalance);
        }
        history.setTransactionCode(transaction.getTransactionCode());
        history.setAccount(transaction.getAccount());
        history.setResponseCode(transaction.getRespCode());
        history.setTransactionDate(transaction.getTransactionDate());
        history.setPhoneNumber(transaction.getAccount().getCustomer().getHomePhone());
        history.setBalance(transaction.getAccount().getBalance());
        return historyRepository.save(history);
    }

    @Override
    public History saveHistoryForCustomerUpdate(Customer customer) {
        log.info("save history for customer update {}", customer.getCif());
        History history = new History();
        history.setCustomer(customer);
        history.setAccount((Account) customer.getAccounts());
        history.setUpdatedAt(LocalDateTime.now());
        history.setBalance(((Account) customer.getAccounts()).getBalance());
        history.setPhoneNumber(customer.getHomePhone());
        return historyRepository.save(history);
    }

    @Override
    public History saveHistoryForAccountUpdate(Account account) {
        log.info("saving history for account update {}", account.getAccountNum());
        History history = new History();
        history.setCustomer(account.getCustomer());
        history.setAccount(account);
        history.setUpdatedAt(LocalDateTime.now());
        history.setBalance(account.getBalance());
        history.setPhoneNumber(account.getCustomer().getHomePhone());
        return historyRepository.save(history);
    }
}
