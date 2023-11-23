package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerCreationException;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.CustomerUpdateDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer saveCustomer(Customer customer);
    boolean updateCustomer(int cif, CustomerUpdateDTO updateDTO) throws CustomerNotFoundException;
    String deleteCustomer(int cif);

    Account saveAccountForCustomer(int cif, Account account);
    List<Account> getShortStatements(int cif);
    Optional<Customer> getCustomerById(int id);
    void validateCustomer(Customer customer);

