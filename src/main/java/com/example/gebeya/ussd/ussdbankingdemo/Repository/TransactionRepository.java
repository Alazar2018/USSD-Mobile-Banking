package com.example.gebeya.ussd.ussdbankingdemo.Repository;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    Slice<Transaction> findTopNByAccountOrderByTransactionDateDesc(Account account, PageRequest pageable);

    List<Transaction> findByAccount(Account accountNum);
}
