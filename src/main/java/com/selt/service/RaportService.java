package com.selt.service;

import com.selt.model.*;
import com.selt.repository.RaportRepo;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class RaportService {

    private final RaportRepo raportRepo;
    private LocalDate date;
    private final TempService tempService;
    private final UserService userService;
    private final TonerService tonerService;
    private final PrinterService printerService;


    public List<Raport> findAll() {
        return raportRepo.findAll();
    }

    public void create(Long tonerId, Long count, Long printerId){
        Raport raport = new Raport();
        Optional<Printer> printer;
        printer = printerService.findById(printerId);
        raport.setToner(tonerService.findById(tonerId).get().getTonerName());
        raport.setCount(count);
        raport.setPrinter(printer.get().getManufacturer()+" " +printer.get().getModel());
        raport.setInventoryNumber(printer.get().getInventoryNumber());
        raport.setDepartment(printer.get().getDepartment().getNameOfDepartment());
        raport.setMPK(printer.get().getDepartment().getMPK().toString());
        raport.setDate(LocalDate.now());
        raport.setUser(userService.findUserByUsername().getFullname());
        raportRepo.save(raport);
    }

    public void save(Raport raport) {
        raport.setDate(LocalDate.now());
        raport.setUser(userService.findUserByUsername().getFullname());
        raportRepo.save(raport);
    }

    public List<Raport> findAllByActualMonth() {
        LocalDate start;
        LocalDate end;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        start = LocalDate.of(year, month, 1);
        int day;

        if (year % 4 == 0) {

            if (month == 2) {
                end = LocalDate.of(year, month, 29);
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                end = LocalDate.of(year, month, 31);
            } else {
                end = LocalDate.of(year, month, 30);
            }
        } else {
            if (month == 2) {
                end = LocalDate.of(year, month, 28);
            } else {
                end = LocalDate.of(year, month, 30);
            }

        }
        return raportRepo.findAllByDateIsBetween(start, end);

    }
    public List<Raport> findAllByPreviousMonth() {
        LocalDate start;
        LocalDate end = null;
        int year;
        int month;

        if(LocalDate.now().getMonthValue()==1){
            month = 12;
            year = LocalDate.now().getYear()-1;
        }
        else {
            month = LocalDate.now().getMonthValue()-1;
            year = LocalDate.now().getYear();
        }
        start = LocalDate.of(year, month, 1);

        if (year % 4 == 0) {

            if (month == 2) {
                end = LocalDate.of(year, month, 29);
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                end = LocalDate.of(year, month, 31);
            } else {
                end = LocalDate.of(year, month, 30);
            }
        } else {
            if (month == 2) {
                end = LocalDate.of(year, month, 28);
            }
            else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                end = LocalDate.of(year, month, 31);
            }
            else {

                end = LocalDate.of(year, month, 30);
            }

        }
        System.out.println(start+" "+ end);
        return raportRepo.findAllByDateIsBetween(start, end);

    }

    public List<Raport> search(String temp) {
        return raportRepo.findAllByPrinterIsLike(temp);
    }

    public List<Raport> findAllByDateBetween(LocalDate start, LocalDate end) {
        return raportRepo.findAllByDateIsBetween(start, end);

    }

    public List<Raport> findAllByTonerIsLike(String temp) {
        return raportRepo.findAllByTonerIsLike(temp);
    }

    public List<Raport> findAllByDepartmentIsLike(String temp) {
        return raportRepo.findAllByDepartmentIsLike(temp);
    }

}

