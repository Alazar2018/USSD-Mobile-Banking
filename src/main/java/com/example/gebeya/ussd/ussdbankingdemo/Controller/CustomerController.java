/*
 * Mobile Banking System
 * 
 * Author Alelign Ayana 
 * 
 */
package com.example.gebeya.ussd.ussdbankingdemo.Controller;


import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.DepositDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations.MobileBankingUserServiceImpl;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.CustomerUpdateDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.TransferDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.WithdrawDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1.2/users")

public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MobileBankingUserServiceImpl mobileBankingUserService;
    private static final Set<String> UPDATABLE_FIELDS = Set.of(
            "firstName", "middleName", "lastName", "email", "dob",
            "homeAddress", "homePhone", "city", "country"
    );


    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/{accountnumber}/balance")
    public ResponseEntity<String> getAmount(@PathVariable int accountnumber) throws AccountNotFoundException {
        Account account = accountService.getAccountByNum(String.valueOf(accountnumber));

        if (account != null) {
            return ResponseEntity.ok("Your Balance is: " + account.getBalance());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for the Account number: " + accountnumber);
        }
    }


    @GetMapping("/{cif}")
    public ResponseEntity<?> getCustomerById(@PathVariable int cif) throws CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerById(cif);

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }



    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        try {
            customerService.validateCustomer(customer); // Custom validation method
            Customer createdCustomer = customerService.saveCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Customer Created Successfully with name " + createdCustomer.getFirstName() +
                            " " + createdCustomer.getLastName() + " and Account Number : " + createdCustomer.getAccount_number());
        } catch (CustomerCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{cif}")
    public ResponseEntity<String> updateCustomer(
            @PathVariable int cif,
            @RequestBody CustomerUpdateDTO updateDTO) throws CustomerNotFoundException {

            // Call the service method with the provided DTO
            boolean result = customerService.updateCustomer(cif, updateDTO);

            if (result) {
                return new ResponseEntity<>("Customer updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No changes made for the customer with CIF: " + cif, HttpStatus.OK);
            }


    }
    @DeleteMapping("/{cif}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int cif) {
        customerService.deleteCustomer(cif);
        return ResponseEntity.noContent().build();
    }



}