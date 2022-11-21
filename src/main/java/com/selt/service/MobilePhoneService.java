package com.selt.service;

import com.selt.model.MobilePhone;
import com.selt.repository.MobilePhoneRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class MobilePhoneService {


    private final MobilePhoneRepo mobilePhoneRepo;

    public List<MobilePhone> findAll(){
        return mobilePhoneRepo.findAll();
    }

    public void save(MobilePhone mobilePhone) {
        mobilePhoneRepo.save(mobilePhone);
    }

    public void delete(MobilePhone mobilePhone) {
        mobilePhoneRepo.delete(mobilePhone);
    }
}
