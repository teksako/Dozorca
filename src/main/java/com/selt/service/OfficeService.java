package com.selt.service;

import com.selt.model.Office;
import com.selt.repository.OfficeRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class OfficeService {


    private final OfficeRepo officeRepo;

    public void save(Office office) {
        officeRepo.save(office);
    }

    public List<Office> findAll() {
        return officeRepo.findAll();

    }

    public List<Office> findAllByHasBeenUse(Boolean use) {
        return officeRepo.findAllByHasBeenUse(use);

    }


    public Optional<Office> findById(long id){
        return officeRepo.findById(id);
    }
    public void delete(Office office) {
        officeRepo.delete(office);
    }


}
