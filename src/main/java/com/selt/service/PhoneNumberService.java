package com.selt.service;

import com.selt.model.PhoneNumber;
import com.selt.repository.PhoneNumberRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class PhoneNumberService {

    private final PhoneNumberRepo phoneNumberRepo;

    public void save(PhoneNumber phoneNumber) {
        phoneNumberRepo.save(phoneNumber);
    }

    public void delete(PhoneNumber phoneNumber) {
        phoneNumberRepo.delete(phoneNumber);
    }

    public List<PhoneNumber> findAll() {
        return phoneNumberRepo.findAll();
    }
}
