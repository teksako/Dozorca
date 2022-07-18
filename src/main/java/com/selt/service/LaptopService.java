package com.selt.service;

import com.selt.model.Laptop;
import com.selt.repository.LaptopRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class LaptopService {


    private final LaptopRepo laptopRepo;

    public void save(Laptop laptop) {
        laptopRepo.save(laptop);
    }

    public void delete(Laptop laptop) {
        laptopRepo.delete(laptop);
    }

    public List<Laptop> findAll() {
        return laptopRepo.findAll();
    }

    public List<Laptop> findAllByEmployee() {
        return laptopRepo.findAllByEmployee_Lastname("Sobolewski");
    }


}
