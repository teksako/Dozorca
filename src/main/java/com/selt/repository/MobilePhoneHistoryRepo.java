package com.selt.repository;

import com.selt.model.MobilePhoneHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobilePhoneHistoryRepo extends JpaRepository<MobilePhoneHistory, Long> {
List<MobilePhoneHistory> findAllByIMEI(String IMEI);
}
