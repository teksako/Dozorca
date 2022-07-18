package com.selt.repository;

import com.selt.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepo extends JpaRepository<Computer, Long> {

    List<Computer> findAllByEmployee_LastnameIs(String employe);
}
