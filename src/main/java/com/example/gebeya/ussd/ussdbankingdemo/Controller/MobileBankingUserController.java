/*
 * Mobile Banking System
 * 
 * Author Alelign Ayana 
 * 
 */
package com.example.gebeya.ussd.ussdbankingdemo.Controller;



import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerCreationException;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.CustomerNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.ErrorResponse;
import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.MobileBankingUserNotFoundException;

import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.CustomerUpdateDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.DepositDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.TransferDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.WithdrawDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.CustomerService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1.2/users/mobile-banking-users")
@Validated

public class MobileBankingUserController {
    @Autowired
    private  MobileBankingUserService mobileBankingUserService;
    @Autowired
    private AccountService accountService;
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<MobileBankingUser>> getAllUsers() {
        List<MobileBankingUser> users = mobileBankingUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/users/{cif}")
    public ResponseEntity<?> createAccountForCustomer(@PathVariable int cif, @RequestBody MobileBankingUser mobileBankingUser) throws MobileBankingUserNotFoundException {
        MobileBankingUser savedUser = mobileBankingUserService.saveMobileBankingUserForCustomer(cif, mobileBankingUser);
        if(savedUser!=null){
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("Couldnt create User check fields", HttpStatus.NOT_ACCEPTABLE);

        }
    }
 @GetMapping("/users/{account}")
 public ResponseEntity<?> getAccountByCif(@PathVariable String account) {
     try {
         Account account1 = accountService.getAccountByNum(Integer.parseInt(account));
         int cif = account1.getCustomer().getCif();
         MobileBankingUser retrievedUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(cif);
         return ResponseEntity.ok(retrievedUser);
     } catch (Exception e) {
         // Log the exception for debugging
         // logger.error("Exception in getAccountByCif endpoint", e);
         ErrorResponse errorResponse = new ErrorResponse("Unexpected error");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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