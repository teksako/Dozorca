package com.selt.repository;

import com.selt.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepo extends JpaRepository<PhoneNumber, Long> {

    List<PhoneNumber> findAllByNumberIsLike(String number);
    List<PhoneNumber> findAllBySIMNumberIsLike(String number);
    List<PhoneNumber> findByOrderByNumberAsc();
}
