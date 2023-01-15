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

    public void save (MobilePhone mobilePhone){
        MobilePhoneHistory mobilePhoneHistory = new MobilePhoneHistory();
        mobilePhoneHistory.setDate(LocalDate.now());
        mobilePhoneHistory.setEmployee(mobilePhone.getEmployee().getFirstname()+" " + mobilePhone.getEmployee().getLastname());
        mobilePhoneHistory.setPhoneNumber(mobilePhone.getPhoneNumber().getNumber());
        mobilePhoneHistory.setSimNumber(mobilePhone.getPhoneNumber().getSIMNumber());
        mobilePhoneHistory.setIMEI(mobilePhone.getIMEI());
        mobilePhoneHistory.setModel(mobilePhone.getModel());
        mobilePhoneHistory.setMAC(mobilePhone.getMAC());
        mobilePhoneHistory.setMark(mobilePhoneHistory.getMark());
        mobilePhoneHistory.setSerialNumber(mobilePhoneHistory.getSerialNumber());
        mobilePhoneHistory.setType("WYDANIE");
        mobilePhoneHistoryRepo.save(mobilePhoneHistory);
    }
}
