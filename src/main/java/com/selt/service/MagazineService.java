package com.selt.service;

import com.selt.model.*;
import com.selt.repository.MagazineRepo;
import com.selt.repository.PrinterRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class MagazineService {

    private final MagazineRepo magazineRepo;
    private final PrinterRepo printerRepo;
    private final RaportService raportService;
    private final PrinterService printerService;
    private final TonerService tonerService;

    public void save(Toner toner){
        Magazine magazine = new Magazine();
        magazine.setCount(0l);
        magazine.setToner(toner);
        magazineRepo.save(magazine);
    }

    public boolean magazineValidation(Long count, Long tonerId){
        if(findByTonerId(tonerId).get().getCount()<count){
            return false;
        } else {
            return true;
        }
    }

    public Long getActualCountByPrinter(Long id) {
        Optional<Magazine> actualCounter = magazineRepo.findById(id);
        return actualCounter.get().getCount();

    }

    public Optional<Magazine> findByTonerId(Long tonerId){
        return Optional.ofNullable(magazineRepo.findByTonerId(tonerId));
    }


    public Long getActualCount(Magazine magazine) {
        Optional<Magazine> actualCounter = magazineRepo.findById(magazine.getId());
        return actualCounter.get().getCount();

    }

    public void updateInventory(Magazine magazine, Long temp) {
        Optional<Magazine> toner1 = magazineRepo.findById(magazine.getId());
        Long add = magazine.getCount();
        add = add + temp;
        toner1.get().setCount(add);
        magazineRepo.save(toner1.get());

    }


    public void removeFromMagazine(Long tonerId, Long temp) {
        Optional<Magazine> magazine = findByTonerId(tonerId);
        magazine.get().setCount(magazine.get().getCount() - temp);
        magazineRepo.save(magazine.get());
    }


    public void save(Magazine magazine) {

        magazineRepo.save(magazine);
    }

    public List<Magazine> findAll() {
        return magazineRepo.findAll();
    }

    public List<Magazine> findAllMagazinesByToner_TonerNameIsLike(String temp) {
        return magazineRepo.findAllMagazinesByToner_TonerNameIsLike(temp);
    }
}
