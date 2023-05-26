package com.selt.service;

import com.selt.model.Computer;
import com.selt.model.MobilePhone;
import com.selt.model.Toner;
import com.selt.repository.ComputerRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ComputerService {

    @Autowired
    private final ComputerRepo computerRepo;

    public List<Computer> findAll() {
        return computerRepo.findAll();
    }

    public List<Computer> findAllByEmployee() {
        return computerRepo.findAllByEmployee_LastnameIs("Sobolewski");
    }

    public void save(Computer computer) {
        computerRepo.save(computer);
    }

    public void delete(long id) {
        Optional<Computer> computer = computerRepo.findById(id);
        computerRepo.delete(computer.get());
    }

    public void update(Computer computer) {
        Optional<Computer> computer1 = computerRepo.findById(computer.getId());
        computer1.get().setIPAdress(computer.getIPAdress());
        computerRepo.save(computer1.get());

    }
}
