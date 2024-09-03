package com.selt.service;

import com.itextpdf.text.DocumentException;
import com.selt.model.Laptop;
import com.selt.model.MobilePhone;
import com.selt.model.Temp;
import com.selt.repository.LaptopRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class LaptopService {


    private final LaptopRepo laptopRepo;
    private final LaptopHistoryService laptopHistoryService;
    private final UserService userService;

    public void save(Laptop laptop) {
        if(laptop.getDemage()==null){
            laptop.setDemage(false);
        }
        if(laptop.getInventoryNumber().isBlank()){
            laptop.setInventoryNumber("-");
        }
        if(laptop.getSerialNumber().isBlank()){
            laptop.setSerialNumber("-");
        }

        laptopRepo.save(laptop);
    }

    public void delete(Long id) {
        Optional<Laptop> laptop = laptopRepo.findById(id);
        laptopRepo.delete(laptop.get());
    }

    public List<Laptop> findAll() {
        return laptopRepo.findAll();
    }

    public List<Laptop> findAllByEmployee() {
        return laptopRepo.findAllByEmployee_Lastname("Sobolewski");
    }


    public Optional<Laptop> findById(long id) {
        return laptopRepo.findById(id);
    }

    public void releaseLaptop(Optional<Laptop> laptop, Temp temp){

        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");
        try {


            if (laptop.get().getEmployee() != null) {
                String pdfName = laptopHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.laptopProtocol(laptop.get(), userService.findUserByUsername().getFullname(), temp, pdfName);
                laptopHistoryService.save(laptop.get(), "WYDANIE", pdfName, LocalDate.parse(temp.getDate()));
                laptop.get().setHasUser(true);
                save(laptop.get());


            }

        } catch (StackOverflowError | IOException | DocumentException e) {
            System.out.println("Nie udało się, wszystkie nazwy są już zajetę!");
            //getAllPhones(message);

        }
    }



    public void getLaptop(Optional<Laptop> laptop, Temp temp){

        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");


        try {
            //String pdfName = mobilePhoneHistoryService.validatePdfName();

            if (laptop.get().getEmployee() != null) {
                String pdfName = laptopHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.laptopProtocol(laptop.get(), userService.findUserByUsername().getFullname(), temp, pdfName);

              laptopHistoryService.save(laptop.get(), "ZDANIE", pdfName, LocalDate.parse(temp.getDate()));
                laptop.get().setHasUser(false);
                laptop.get().setEmployee(null);
                save(laptop.get());
                //savePdf(mobilePhone.get());
            }


        } catch (StackOverflowError | IOException | DocumentException e) {
            System.out.println("Nie udało się, wszystkie nazwy są już zajetę!");
        }
    }

}
