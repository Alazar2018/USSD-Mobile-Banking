package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerCreationException;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.CustomerUpdateDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.AccountRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.CustomerRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.CustomerService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accounts;

    @Autowired
    private TransactionServiceImpl transaction;

    @Autowired
    private MobileBankingUserServiceImpl mobileBankingUser;
    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public List<Customer> getAllCustomers() {
        log.info("getting all customers");
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        log.info("saving customer {}", customer.getCif());
        // Generate an account number starting with 1000 and having a total length of 12 digits
        String generatedAccountNumber = generateAccountNumber();

        // Set the generated account number to the customer
        customer.setAccount_number(generatedAccountNumber);

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        return customerRepository.save(savedCustomer);
    }

    @Override
    @Transactional
    public boolean updateCustomer(int cif, CustomerUpdateDTO updateDTO) throws CustomerNotFoundException {
        log.info("updating customer {}", cif);
        // Implementation for updateCustomer method
        Customer existingCustomer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException());

        // Check if any updates are needed
        boolean changesMade = applyUpdates(existingCustomer, updateDTO);

        if (changesMade) {
            customerRepository.save(existingCustomer);
        }

        return changesMade;
    }

    private boolean applyUpdates(Customer existingCustomer, CustomerUpdateDTO updateDTO) {
        log.info("applying updates to customer", existingCustomer.getCif());
        boolean changesMade = false;

        // Get all methods in CustomerUpdateDTO class
        Method[] methods = CustomerUpdateDTO.class.getDeclaredMethods();

        for (Method method : methods) {
            // Check if it's a getter
            if (isGetter(method)) {
                try {
                    // Invoke the getter to get the value from updateDTO
                    Object value = method.invoke(updateDTO);

                    // Check if the value is not null
                    if (value != null) {
                        // Build the corresponding setter method name
                        String setterName = "set" + method.getName().substring(3);
                        // Get the setter method
                        Method setter = Customer.class.getMethod(setterName, method.getReturnType());
                        // Use reflection to set the corresponding field in the existingCustomer
                        setter.invoke(existingCustomer, value);

                        // Changes were made
                        changesMade = true;
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    // Handle exceptions if needed
                    e.printStackTrace();
                }
            }
        }

        return changesMade;
    }

    // Check if a method is a getter
    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") && method.getParameterCount() == 0;
    }


    @Override
    @Transactional
    public String deleteCustomer(int cif) {
        log.info("removing customer {}", cif);
        customerRepository.deleteById(cif);
        return "Customer Removed ||" + cif;
    }

    @Override
    public Account saveAccountForCustomer(int cif, Account account) throws CustomerNotFoundException {
        // Implementation for saveAccountForCustomer method
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException());
        account.setAccountNum(Integer.parseInt(customer.getAccount_number()));
        account.setCustomer(customer);
        return accounts.saveAccount(account);
        }

    @Override
    public List<Account> getShortStatements(int cif) throws CustomerNotFoundException {
        log.info("getting short statement for customer {}", cif);
        // Implementation for getShortStatements method
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException());

        List<Account> customerAccounts = customer.getAccounts();

        // Assuming each customer can have multiple accounts
        for (Account account : customerAccounts) {
            List<Transaction> limitedTransactions = transaction.getByAccount(account);
            account.setTransactions(limitedTransactions);
        }

        return customerAccounts;
    }

    @Override
    public Optional<Customer> getCustomerById(int cif) {
        return customerRepository.findById(cif);
    }

    @Override
    public void validateCustomer(Customer customer) throws CustomerCreationException {
        // Implementation for validateCustomer method
        if (customer == null || customer.getFirstName() == null || customer.getMiddleName() == null || customer.getLastName() == null || customer.getDob() == null || customer.getEmail() == null) {
            throw new CustomerCreationException("Customer details are incomplete");
        }
    }

    private String generateAccountNumber() {
        // Generate a random number between 100 and 199
        int randomNumber = new Random().nextInt(1000) + 1000;

        // Combine the prefix "1000" with the random number to create the account number
        return "1000" + randomNumber;
    }


}
