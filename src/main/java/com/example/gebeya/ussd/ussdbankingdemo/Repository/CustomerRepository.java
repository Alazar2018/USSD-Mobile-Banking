package com.example.gebeya.ussd.ussdbankingdemo.Repository;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
