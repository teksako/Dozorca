package com.selt.service;

import com.selt.model.PhoneNumber;
import com.selt.repository.PhoneNumberRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return cleanBlankNumber(phoneNumberRepo.findAll());
    }

    public List<PhoneNumber> cleanBlankNumber(List<PhoneNumber> phoneNumbers) {
        List<PhoneNumber> phoneNumbers1 = new ArrayList<>();
        for (PhoneNumber number : phoneNumbers) {
            if (!number.getNumber().equals("-")) {
                phoneNumbers1.add(number);
            }
        }
        return phoneNumbers1;
    }

    public List<PhoneNumber> findBlankNumber(List<PhoneNumber> phoneNumbers){
        List<PhoneNumber> phoneNumbers1 = new ArrayList<>();
        for (PhoneNumber number : phoneNumbers) {
            if (number.getNumber().equals("-")) {
                phoneNumbers1.add(number);
            }
        }
        return phoneNumbers1;
    }

    public List<PhoneNumber> findAllByNumberIsLike(String number){
        return phoneNumberRepo.findAllByNumberIsLike(number);
    }

    public List<PhoneNumber> findAllBySIMNumberIsLike(String number){
        return phoneNumberRepo.findAllBySIMNumberIsLike(number);
    }


}
