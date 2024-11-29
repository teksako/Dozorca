package com.selt.service;

import com.selt.model.Computer;
import com.selt.repository.ComputerRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ComputerService {

    @Autowired
    private final ComputerRepo computerRepo;
    private final WakeOnLan wakeOnLan;

    public List<Computer> findAll() {
        return computerRepo.findAll();
    }
    public List<Computer> findAllByEmployee_IdIs(long id){
        return computerRepo.findAllByEmployee_IdIs(id);
    }



    public void save(Computer computer) {
        computerRepo.save(computer);
    }

    public void delete(long id) {
        Optional<Computer> computer = computerRepo.findById(id);
        computerRepo.delete(computer.get());
    }

    public void update(Computer computer) {
        Optional<Computer> computer1 = findById(computer.getId());
        computer1.get().setIPAdress(computer.getIPAdress());
        computerRepo.save(computer1.get());

    }

    public Optional<Computer> findById(long id){
        return computerRepo.findById(id);
    }

    public void wakeUp(long id){
        wakeOnLan.sentPacket("255.255.255.255",findById(id).get().getMACAdress());

    }
}
