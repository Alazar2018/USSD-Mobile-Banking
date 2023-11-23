package com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.MobileBankingUserNotFoundException;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;

import java.util.List;

public interface MobileBankingUserService {
    List<MobileBankingUser> getAllUsers();
    MobileBankingUser saveMobileBankingUserForCustomer(int cif, MobileBankingUser mobileBankingUser) throws MobileBankingUserNotFoundException;
    MobileBankingUser getMobileBankingUserDetailsForCustomer(int cif) throws MobileBankingUserNotFoundException;
    Account getAccountByCif(int cif, Account account) throws MobileBankingUserNotFoundException;
    MobileBankingUser getMobileBankingUserByAccount(Account account);
}

