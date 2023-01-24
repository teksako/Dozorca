package com.selt.service;

import com.selt.model.Location;
import com.selt.model.PhoneNumber;
import com.selt.repository.PhoneNumberRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class PhoneNumberService {

    private final PhoneNumberRepo phoneNumberRepo;

    public void save(PhoneNumber phoneNumber) {
        phoneNumberRepo.save(phoneNumber);
    }

    public void delete(long id) {
        Optional<PhoneNumber> phoneNumber = phoneNumberRepo.findById(id);

        phoneNumberRepo.delete(phoneNumber.get());
    }

    public List<PhoneNumber> findByOrderByNumberAsc(){
        return phoneNumberRepo.findByOrderByNumberAsc();
    }

    public List<PhoneNumber> findAll() {
        return phoneNumberRepo.findAll();
    }


    private  static PhoneNumber phoneNumber = new PhoneNumber();
    public List<PhoneNumber> cleanBlankNumber(List<PhoneNumber> phoneNumbers) {
        List<PhoneNumber> phoneNumbers1 = new ArrayList<>();
        for (PhoneNumber number : phoneNumbers) {
            if (!number.getNumber().equals("-")) {
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
