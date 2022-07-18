package com.selt.service;

import com.selt.model.Office;
import com.selt.repository.OfficeRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void delete(Office office) {
        officeRepo.delete(office);
    }
}
