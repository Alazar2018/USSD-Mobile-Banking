package com.example.gebeya.ussd.ussdbankingdemo.Repository;

import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Integer> {
}
