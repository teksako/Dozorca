package com.selt.service;

import com.selt.model.*;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Data
@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class PrinterService {


    private final PrinterRepo printerRepo;
    private final OIDRepo oidRepo;


    public Optional<Printer> findById(Long id) {
        return printerRepo.findById(id);
    }

    public List<Printer> findAll() {
        return printerRepo.findAll();
    }

//    @Scheduled(fixedDelay = 1000)
//    public void print(){
//        System.out.println(randomNumber());
//    }

    public int randomNumber() {
        int min = 0;
        int max = 100;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public List<String> getIP() {
        List<Printer> printerList = printerRepo.findAll();
        List<String> IPAdress = new ArrayList<>();
        for (Printer printer : printerList) {
            String model = printer.getManufacturer();
            if (model.equals("Konica Minolta") && !printer.getIPAdress().equals("-")) {

                IPAdress.add(printer.getIPAdress());
            }
        }
        return IPAdress;
    }

    //@Scheduled(cron = "0 39 14 ? * TUE")
    //@Scheduled(fixedDelay = 1000000)


    public List<Toner> findAlltoner(long id) {
        Optional<Printer> printer = printerRepo.findById(id);
        List<Toner> tonerList = printer.get().getTonerList();
        return tonerList;
    }

    public List<OID> findActualUse(long id) {
        Optional<Printer> printer = printerRepo.findById(id);
        List<OID> actualList = printer.get().getOid();
        List<OID> findAll = oidRepo.findAll();
        List<OID> finallyList = new ArrayList<>();
        for (OID list : findAll) {
            if (actualList.size() != 0) {
                for (OID list2 : actualList) {
                    if (list.equals(list2)) {
                        finallyList.add(list);
                    }
                }
                //finallyList.remove(list);
            } else {
                finallyList = findAll;
            }

        }
        return findAll;
    }


    public Optional<Printer> findById(long id) {
        return printerRepo.findById(id);
    }


    public void save(Printer printer) {

        if (printer.getIPAdress().isBlank()) {
            printer.setIPAdress("-");
        }
        printerRepo.save(printer);
    }

    public void deletePrinter(long id) {
        Optional<Printer> printer1 = printerRepo.findById(id);
        printerRepo.delete(printer1.get());
    }

    public void editPrinter(long id) {
        Optional<Printer> printer1 = printerRepo.findById(id);
        printerRepo.save(printer1.get());
    }

    public List<Printer> findAllByModelIsLike(String model) {
        return printerRepo.findAllByModelIsLike(model);
    }

}
