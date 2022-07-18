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


    public List<Temp> findAll(){
        return tempRepo.findAll();
    }

//    public LocalDate returnStartDate(Temp temp){
//        return temp.getStart();
//    }

//    public LocalDate returnEndDate(Temp temp){
//        return temp.getEnd();
//    }
}
