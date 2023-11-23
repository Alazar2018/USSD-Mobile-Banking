package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.MobileBankingUserNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.CustomerRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.MobileBankingUserRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service implementation class for managing mobile banking users.
 *
 *
 * This class provides methods for retrieving, saving, and updating mobile banking user details.
 * It interacts with the {@link MobileBankingUserRepository} and {@link CustomerRepository}
 * for persistence and customer-related operations.
 *
 *
 * <p>
 * @author Alazar Tilahun
 * </p>
 */
@Service
public class MobileBankingUserServiceImpl implements MobileBankingUserService {
    @Autowired
    private MobileBankingUserRepository mobileBankingUserRepository;

    @Autowired
    private CustomerRepository customerRepository;
    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    @Override
    public List<MobileBankingUser> getAllUsers() {
        log.info("getting all users");
        return mobileBankingUserRepository.findAll();
    }

    @Override
    @Transactional
    public MobileBankingUser saveMobileBankingUserForCustomer(int cif, MobileBankingUser mobileBankingUser) throws MobileBankingUserNotFoundException {
        log.info("saving mobile banking user {}", cif);
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new MobileBankingUserNotFoundException());
        mobileBankingUser.setCustomer(customer);
        return mobileBankingUserRepository.save(mobileBankingUser);
    }

    @Override
    @Transactional
    public MobileBankingUser getMobileBankingUserDetailsForCustomer(int cif) throws MobileBankingUserNotFoundException {
        log.info("getting mobile banking user {}", cif);
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new MobileBankingUserNotFoundException());
        MobileBankingUser mobileBankingUser = new MobileBankingUser();
        mobileBankingUser.setCustomer(customer);
        return mobileBankingUser.getCustomer().getMobileBankingUsers();
    }

    @Override
    @Transactional
    public Account getAccountByCif(int cif, Account account) throws MobileBankingUserNotFoundException {
        log.info("getting account by cif {}", cif);
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new MobileBankingUserNotFoundException());

        account.setCustomer(customer);
        return account;
    }

    @Override
    @Transactional
    public MobileBankingUser getMobileBankingUserByAccount(Account account) {
        log.info("getting mobile banking user by account {}", account.getAccountNum());
        MobileBankingUser mobileBankingUser = new MobileBankingUser();
        mobileBankingUser.setCustomer(account.getCustomer());
        return mobileBankingUser;
    }
}
