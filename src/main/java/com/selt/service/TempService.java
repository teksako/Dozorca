package com.selt.service;

import com.selt.model.Temp;
import com.selt.repository.TempRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Data
public class TempService {

    private final TempRepo tempRepo;

    public int randomNumber() {
        int min = 10000;
        int max = 99999;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }


    public List<Temp> findAll(){
        return tempRepo.findAll();
    }


}
