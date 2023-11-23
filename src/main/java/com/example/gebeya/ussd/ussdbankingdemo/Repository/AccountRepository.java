package com.example.gebeya.ussd.ussdbankingdemo.Repository;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    List<Account> findAllByCustomerCif(int cif);
}
