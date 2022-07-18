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

//    public Optional<Magazine> findMagazine(Printer printer) {
//        Optional<Magazine> magazine = magazineRepo.findById(getFromPrinter(printer));
//        return magazine;
//
//    }
//
//    public Long getFromPrinter(Printer printer) {
//        Optional<Printer> printer1 = printerRepo.findById(printer.getId());
//        Long tonerId = printer1.get().getToner().getId();
//        return tonerId;
//    }

    public void updateInventory(Magazine magazine, Long temp) {
        Optional<Magazine> toner1 = magazineRepo.findById(magazine.getId());
        Long add = magazine.getCount();
        add = add + temp;
        toner1.get().setCount(add);
        magazineRepo.save(toner1.get());

    }
//-------------pierwotna wersja---------------------------------
//    public void removeInventory(Magazine magazine, Long temp) {
//        Optional<Magazine> toner1 = magazineRepo.findById(magazine.getId());
//        Long add= magazine.getCount();
//        add=temp-add;
//        toner1.get().setCount(add);
//        magazineRepo.save(toner1.get());
//
//    }

//    ------druga wersja bez licznika----------------------------
//    public void removeInventory(Printer printer, int temp) {
//
//        Magazine magazine=new Magazine();
//       //Optional<Magazine> toner1 = getFromPrinter(printer);
//        Optional<Magazine> toner1 = magazineRepo.findById(getFromPrinter(printer));
//        //Long temp = 2l;
//        Long add=toner1.get().getCount();
//       // Long add= magazine.getCount();
//        add=add-temp;
//        toner1.get().setCount(add);
//        magazineRepo.save(toner1.get());
//
//    }


    public void removeFromMagazine(Long tonerId, Long temp) {
        Optional<Magazine> magazine = findByTonerId(tonerId);
        magazine.get().setCount(magazine.get().getCount() - temp);
        magazineRepo.save(magazine.get());
    }
//-------------------------pierwotne wydawanie z magazynu----------------------------
//    public void removeInventory(Magazine magazine, Long temp) {
//        // Raport raport = new Raport();
//        Optional<Magazine> toner1 = magazineRepo.findById(printerService.getTonerId(magazine.getId()));
//        toner1.get().setCount(temp - magazine.getCount());
//        magazineRepo.save(toner1.get());
//        Optional<Printer> printer = printerRepo.findById(magazine.getId());
//        raport.setPrinters(printer.get());
//        raport.setCount(magazine.getCount());
//        raportService.save(raport);
//
//    }

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
