package com.selt.repository;

import com.selt.model.MobilePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobilePhoneRepo extends JpaRepository<MobilePhone, Long> {
}
