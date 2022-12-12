package com.selt.service;

import com.itextpdf.text.DocumentException;
import com.selt.model.MobilePhone;
import com.selt.repository.MobilePhoneRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class MobilePhoneService {


    private final MobilePhoneRepo mobilePhoneRepo;
    private final EmployeeService employeeService;
    private final PhoneNumberService numberService;
    private final UserService userService;
    //private final ExportPDF exportPDF;

    public List<MobilePhone> findAll() {
        return mobilePhoneRepo.findAll();
    }

    public void save(MobilePhone mobilePhone) throws DocumentException, IOException {

//        if (mobilePhone.getPhoneNumber() == null) {
//            mobilePhone.setPhoneNumber(numberService.findBlankNumber().get(0));
//        }
//        if (mobilePhone.getEmployee() == null) {
//            mobilePhone.setEmployee(employeeService.findBlankEmployeer().get(0));
//        }
       // ExportPDF.protcol(mobilePhone,userService.findUserByUsername().getFullname());

        mobilePhoneRepo.save(mobilePhone);
    }

    public List<MobilePhone> search(String matter){
        return mobilePhoneRepo.findAllByModelIsLike(matter);
    }

    public void delete(MobilePhone mobilePhone) {
        mobilePhoneRepo.delete(mobilePhone);
    }
}
