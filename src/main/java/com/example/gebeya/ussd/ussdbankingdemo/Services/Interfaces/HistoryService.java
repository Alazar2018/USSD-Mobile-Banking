package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.History;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;

import java.util.List;

public interface HistoryService {
    List<History> getAllHistories();

    History getHistoryByRrn(int rrn);

    History saveHistory(History history);

    void deleteHistory(int rrn);

    History saveHistoryForCustomerCreated(Customer customer);

    History saveHistoryForTransaction(Transaction transaction);

    History saveHistoryForCustomerUpdate(Customer customer);

    History saveHistoryForAccountUpdate(Account account);
}
