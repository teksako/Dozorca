package com.selt.service;

import com.selt.model.MobilePhone;
import com.selt.model.MobilePhoneHistory;
import com.selt.repository.MobilePhoneHistoryRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class MobilePhoneHistoryService {

    private final MobilePhoneHistoryRepo mobilePhoneHistoryRepo;
    private final UserService userService;
    private final TempService tempService;


    public List<MobilePhoneHistory> findAllByIMEI(String IMEI) {
        return mobilePhoneHistoryRepo.findAllByIMEI(IMEI);
    }

    //    public Boolean validatePdfName(String pdfName) {
//        //String pdfName = LocalDate.now() + "-" + tempService.randomNumber();
//        for (MobilePhoneHistory mobilePhoneHistory : mobilePhoneHistoryRepo.findAll()) {
//            if (mobilePhoneHistory.getProtocolName().equals(pdfName)) {
//                System.out.println("taki kwit istnieje");
//                return false;
//            }
//        }
//        System.out.println("nie ma takiego dokumentu, został utworzony");
//
//     return true;
//    }
    public String validatePdfName() {
        String pdfName = LocalDate.now() + "-" + tempService.randomNumber();
        for (MobilePhoneHistory mobilePhoneHistory : mobilePhoneHistoryRepo.findAll()) {
            if (mobilePhoneHistory.getProtocolName().equals(pdfName)) {
                System.out.println("taki kwit istnieje " + pdfName);
                return validatePdfName();
            }
        }
        System.out.println("nie ma takiego dokumentu, został utworzony");

        return pdfName;
    }

    public void save(MobilePhone mobilePhone, String type, String pdfName) {
        MobilePhoneHistory mobilePhoneHistory = new MobilePhoneHistory();
        mobilePhoneHistory.setDate(LocalDate.now());
        mobilePhoneHistory.setEmployee(mobilePhone.getEmployee().getFirstname() + " " + mobilePhone.getEmployee().getLastname());
        mobilePhoneHistory.setPhoneNumber(mobilePhone.getPhoneNumber().getNumber());
        mobilePhoneHistory.setSimNumber(mobilePhone.getPhoneNumber().getSIMNumber());
        mobilePhoneHistory.setIMEI(mobilePhone.getIMEI());
        mobilePhoneHistory.setSerialNumber(mobilePhone.getSerialNumber());
        mobilePhoneHistory.setSerialNumber(mobilePhoneHistory.getSerialNumber());
        mobilePhoneHistory.setProtocolName(pdfName);
        mobilePhoneHistory.setType(type);
        mobilePhoneHistory.setUser(userService.actualLoginUser());
        mobilePhoneHistoryRepo.save(mobilePhoneHistory);
    }

    public Optional<MobilePhoneHistory> findById(long id) {
        return mobilePhoneHistoryRepo.findById(id);
    }
}
