package com.selt.repository;

import com.selt.model.Laptop;
import com.selt.model.LaptopHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LaptopHistoryRepo extends JpaRepository<LaptopHistory, Long> {

    List<LaptopHistory> findAll();
    List<LaptopHistory> findAllByModel(String model);
    List<LaptopHistory> findAllByEmployee(String employee);
    List<LaptopHistory> findAllBySerialNumber(String serialnumber);
    List<LaptopHistory> findAllByProtocolName(String protocolName);
    List<LaptopHistory> findAllByDateIsBetween(LocalDate start, LocalDate end);

}
