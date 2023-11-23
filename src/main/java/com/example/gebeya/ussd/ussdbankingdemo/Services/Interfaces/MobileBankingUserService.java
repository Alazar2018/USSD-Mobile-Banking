package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;

import java.util.List;

public interface MobileBankingUserService {
    List<MobileBankingUser> getAllUsers();
    MobileBankingUser saveMobileBankingUserForCustomer(int cif, MobileBankingUser mobileBankingUser);
    MobileBankingUser getMobileBankingUserDetailsForCustomer(int cif);
    Account getAccountByCif(int cif, Account account);
    MobileBankingUser getMobileBankingUserByAccount(Account account);
}

