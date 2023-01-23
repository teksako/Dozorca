package com.selt.service;

import com.selt.model.MobilePhone;
import com.selt.model.MobilePhoneHistory;
import com.selt.repository.MobilePhoneHistoryRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Data
@RequiredArgsConstructor
public class MobilePhoneHistoryService {

    private final MobilePhoneHistoryRepo mobilePhoneHistoryRepo;

    public void save (MobilePhone mobilePhone, String type){
        MobilePhoneHistory mobilePhoneHistory = new MobilePhoneHistory();
        mobilePhoneHistory.setDate(LocalDate.now());
        mobilePhoneHistory.setEmployee(mobilePhone.getEmployee().getFirstname()+" " + mobilePhone.getEmployee().getLastname());
        mobilePhoneHistory.setPhoneNumber(mobilePhone.getPhoneNumber().getNumber());
        mobilePhoneHistory.setSimNumber(mobilePhone.getPhoneNumber().getSIMNumber());
        mobilePhoneHistory.setIMEI(mobilePhone.getIMEI());
        mobilePhoneHistory.setSerialNumber(mobilePhone.getSerialNumber());
        mobilePhoneHistory.setSerialNumber(mobilePhoneHistory.getSerialNumber());
        mobilePhoneHistory.setType(type);
        mobilePhoneHistoryRepo.save(mobilePhoneHistory);
    }
}
