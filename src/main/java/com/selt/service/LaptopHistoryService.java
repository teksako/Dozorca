package com.selt.service;

import com.selt.model.Laptop;
import com.selt.model.LaptopHistory;
import com.selt.model.MobilePhone;
import com.selt.model.MobilePhoneHistory;
import com.selt.repository.LaptopHistoryRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Data
@RequiredArgsConstructor
@Service
public class LaptopHistoryService {

    private final LaptopHistoryRepo laptopHistoryRepo;
    private final TempService tempService;
    private final UserService userService;

    public List<LaptopHistory> findAll() {
        return laptopHistoryRepo.findAll();
    }

    public void save(Laptop laptop, String type, String pdfName, LocalDate date) {
        LaptopHistory laptopHistory = new LaptopHistory();
        laptopHistory.setDate(date);
        laptopHistory.setEmployee(laptop.getEmployee().getFirstname() + " " + laptop.getEmployee().getLastname());
        laptopHistory.setInventoryNumber(laptop.getInventoryNumber());
        laptopHistory.setManufacturer(laptop.getManufacturer());
        laptopHistory.setModel(laptop.getModel());
        laptopHistory.setProtocolName(pdfName);
        laptopHistory.setSerialNumber(laptop.getSerialNumber());
        laptopHistory.setType(type);
        laptopHistory.setUser(userService.actualLoginUser());
       laptopHistoryRepo.save(laptopHistory);
    }

    public void delete(LaptopHistory history) {
        laptopHistoryRepo.delete(history);
    }

    public List<LaptopHistory> findAllByModel(String model) {
        return laptopHistoryRepo.findAllByModel(model);
    }

    public List<LaptopHistory> findAllByEmployee(String employee) {
        return laptopHistoryRepo.findAllByEmployee(employee);
    }

    public List<LaptopHistory> findAllBySerialNumber(String serialnumber) {
        return laptopHistoryRepo.findAllBySerialNumber(serialnumber);
    }

    public List<LaptopHistory> findAllByProtocolName(String protocolName) {
        return laptopHistoryRepo.findAllByProtocolName(protocolName);
    }

    public List<LaptopHistory> findAllByDateIsBetween(LocalDate start, LocalDate end) {
        return laptopHistoryRepo.findAllByDateIsBetween(start, end);
    }

    public Optional<LaptopHistory> findById(long id) {

        return laptopHistoryRepo.findById(id);
    }

    public String validatePdfName(LocalDate date) {
        String pdfName = date + "-" + tempService.randomNumber();
        for (LaptopHistory laptopHistory : findAll()) {
            if (laptopHistory.getProtocolName().equals(pdfName)) {
                //System.out.println("taki kwit istnieje " + pdfName);
                return validatePdfName(date);
            }
        }
        System.out.println("Dokument " + pdfName + " został utworzony oraz zapisany");

        return pdfName;
    }


}
