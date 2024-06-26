package com.selt.service;

import com.selt.model.LaptopHistory;
import com.selt.model.MobilePhoneHistory;
import com.selt.model.Temp;
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
public class LaptopHistoryservice {

    private final LaptopHistoryRepo laptopHistoryRepo;
    private final TempService tempService;

    public List<LaptopHistory> findAll() {
        return laptopHistoryRepo.findAll();
    }

    public void save(LaptopHistory history) {
        laptopHistoryRepo.save(history);
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
