/*
 * Mobile Banking System
 * 
 * Author Alelign Ayana 
 * 
 */
package com.example.gebeya.ussd.ussdbankingdemo.Controller;
import com.example.gebeya.ussd.ussdbankingdemo.exceptions.AccountNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.exceptions.handler.CustomExceptionHandler;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.DepositDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.TransferDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.WithdrawDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.CustomerRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1.2/users/accounts")

public class AccountController {
    @Autowired
    private  AccountService accountService;
    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private MobileBankingUserService mobileBankingUserService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(accounts);
        }
    }

    @GetMapping("/{accountNum}")
    public ResponseEntity<?> getAccountByNum(@PathVariable int accountNum) {
        Account account = accountService.getAccountByNum(accountNum);

        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for number: " + accountNum);
        }
    }

    @PostMapping("/customers/{cif}")
    public ResponseEntity<?> createAccountForCustomer(@PathVariable int cif, @RequestBody Account account) {
        Account savedAccount = accountService.saveAccountForCustomer(cif, account);

        if (savedAccount != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
        } else {
            // Return a more specific error message or a custom response object
            String errorMessage = "Failed to create account for customer with CIF: " + cif;
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }
    }
@PostMapping("/customers/deposit")
    public ResponseEntity<?> depositMoney(@RequestBody DepositDTO requestDTO) {
        try {
            Account account = accountService.getAccountByNum(requestDTO.getAccountNumber());
            MobileBankingUser mobileBankingUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(account.getCustomer().getCif());
            String storedPassword = mobileBankingUser.getPassword();
            String enteredPassword = requestDTO.getPassword();
            if (requestDTO.getAmount() != null && enteredPassword != null &&storedPassword!=null) {
                if (enteredPassword.equals(storedPassword)) {
                    BigDecimal amount = requestDTO.getAmount();
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        return ResponseEntity.badRequest().body("'amount' must be a positive value");
                    }
                    String depositResult = accountService.depositMoney(String.valueOf(requestDTO.getAccountNumber()), amount);
                    return ResponseEntity.ok(depositResult);
                } else {
                    return ResponseEntity.badRequest().body("'Password' doesn't match");
                }
            } else if (requestDTO.getCustomerAccountNumber() != 0  requestDTO.getOtp() != null && enteredPassword != null && enteredPassword.equals(storedPassword)) {
                int customerAccountNumber = requestDTO.getCustomerAccountNumber();
                Account customerAccount = accountService.getAccountByNum(customerAccountNumber);
                String otp = requestDTO.getOtp();
                if(account.getAccountNum()!=customerAccount.getAccountNum()){
                    String verifyDeposit = accountService.verifyDeposit(account.getAccountNum(), customerAccount.getAccountNum(), otp);
                    return new ResponseEntity<>(verifyDeposit, HttpStatus.OK);}
            }

        } catch (AccountNotFoundException e) {
       
        e.printStackTrace();  
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    } catch (javax.security.auth.login.AccountNotFoundException e) {
     
        e.printStackTrace();  
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    } catch (Exception e) {
     
        e.printStackTrace();  
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
    }

    return ResponseEntity.badRequest().body("Are you a mobile banking user!. Invalid deposit request, check input fields, you need amount, account, password");
}

    @PostMapping("/customers/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody WithdrawDTO requestDTO) {
        try {
            Account account = accountService.getAccountByNum(requestDTO.getAccountNumber());
            MobileBankingUser mobileBankingUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(account.getCustomer().getCif());
            String verfiypass=mobileBankingUser.getPassword();
            String password=requestDTO.getPassword();
            if (requestDTO.getAmount() != null&&password!=null) {
                if(password.equals(verfiypass)){
                    BigDecimal amount = requestDTO.getAmount();

                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        return new ResponseEntity<>("'amount' must be a positive value", HttpStatus.BAD_REQUEST);
                    }

                    String withdrawResult = accountService.withdrawMoney(Integer.parseInt(String.valueOf(requestDTO.getAccountNumber())), amount);
                    return new ResponseEntity<>(withdrawResult, HttpStatus.OK);
                }else{
                	return new ResponseEntity<>("Password Doesnt match", HttpStatus.BAD_REQUEST);}


} else if (requestDTO.getCustomerAccountNumber() != 0  requestDTO.getOtp() != null&&password!=null&&password==mobileBankingUser.getPassword()) {
                int customerAccountNumber = requestDTO.getCustomerAccountNumber();
                Account customerAccount = accountService.getAccountByNum(customerAccountNumber);
                String otp = requestDTO.getOtp();
                if(customerAccount.getAccountNum()!=account.getAccountNum()){
                    String verifyDeposit = accountService.verifyWithdraw(account.getAccountNum(), customerAccount.getAccountNum(), otp);
                    return new ResponseEntity<>(verifyDeposit, HttpStatus.OK);}
                else{
                    return new ResponseEntity<>("Please insert different account number for merchant and customer", HttpStatus.NOT_ACCEPTABLE);
                }

            }

        } catch (AccountNotFoundException | javax.security.auth.login.AccountNotFoundException e) {
           
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
           
            return new ResponseEntity<>("Unexpected error at depositMoney endpoint ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Invalid Withdraw request", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/customers/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferDTO requestDTO) throws javax.security.auth.login.AccountNotFoundException {

        String senderAccount=requestDTO.getSenderAccountNumber();
        String receiverAccount=requestDTO.getReceiverAccountNumber();
        Account sender=accountService.getAccountByNum(Integer.parseInt(senderAccount));
        MobileBankingUser mobileBankingUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(sender.getCustomer().getCif());
        String verfiypass=mobileBankingUser.getPassword();
        String password=requestDTO.getPassword();
        BigDecimal amount=requestDTO.getAmount();
        if(amount!=null&&senderAccount!=null&&receiverAccount!=null&&password!=null) {
            if(password.equals(verfiypass)){
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    return new ResponseEntity<>("'amount' must be a positive value", HttpStatus.BAD_REQUEST);
                } else {
                    String transferMessage=accountService.transactionMoney(senderAccount,receiverAccount,amount);
                    return new ResponseEntity<>(transferMessage, HttpStatus.OK);
                }

            }else{return new ResponseEntity<>("Password Doesnt match", HttpStatus.BAD_REQUEST);}

        }else{
            return new ResponseEntity<>("Fields are null", HttpStatus.BAD_REQUEST);

        }

    }
  

    @DeleteMapping("/{accountNum}")
    public ResponseEntity<String> deleteAccount(@PathVariable int accountNum) {
        boolean deletionMessage = accountService.deleteAccount(accountNum);

        if (!deletionMessage) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Doesn't Exist");
        } else {
            return ResponseEntity.ok("Deleted Successfully");
        }
    }


}