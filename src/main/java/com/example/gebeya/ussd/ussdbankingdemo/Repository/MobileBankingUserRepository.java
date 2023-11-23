package com.example.gebeya.ussd.ussdbankingdemo.Repository;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileBankingUserRepository extends JpaRepository<MobileBankingUser,Integer> {
}
