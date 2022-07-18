package com.selt.repository;

import com.selt.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepo extends JpaRepository<Laptop, Long> {

    List<Laptop> findAllByEmployee_Lastname(String lastname);
}
