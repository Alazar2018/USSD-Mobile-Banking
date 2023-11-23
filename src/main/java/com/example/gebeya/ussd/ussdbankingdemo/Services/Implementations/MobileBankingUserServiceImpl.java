package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.CustomerRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.MobileBankingUserRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MobileBankingUserServiceImpl implements MobileBankingUserService {
    @Autowired
    private MobileBankingUserRepository mobileBankingUserRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<MobileBankingUser> getAllUsers() {
        return mobileBankingUserRepository.findAll();
    }

    @Override
    @Transactional
    public MobileBankingUser saveMobileBankingUserForCustomer(int cif, MobileBankingUser mobileBankingUser) {
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException("Mobile banking user not found for customer with CIF:" + cif));
        mobileBankingUser.setCustomer(customer);
        return mobileBankingUserRepository.save(mobileBankingUser);
    }

    @Override
    @Transactional
    public MobileBankingUser getMobileBankingUserDetailsForCustomer(int cif) {
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new MobileBankingUserNotFoundException("Mobile banking user not found for customer with CIF: " + cif));
        MobileBankingUser mobileBankingUser = new MobileBankingUser();
        mobileBankingUser.setCustomer(customer);
        return mobileBankingUser.getCustomer().getMobileBankingUsers();
    }

    @Override
    @Transactional
    public Account getAccountByCif(int cif, Account account) {
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with CIF: " + cif));

        account.setCustomer(customer);
        return account;
    }

    @Override
    @Transactional
    public MobileBankingUser getMobileBankingUserByAccount(Account account) {
        MobileBankingUser mobileBankingUser = new MobileBankingUser();
        mobileBankingUser.setCustomer(account.getCustomer());
        return mobileBankingUser;
    }
}
