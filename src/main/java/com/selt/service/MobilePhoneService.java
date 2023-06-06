package com.selt.service;

import com.itextpdf.text.DocumentException;
import com.selt.model.MobilePhone;
import com.selt.repository.MobilePhoneRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class MobilePhoneService {


    private final MobilePhoneRepo mobilePhoneRepo;
    private final EmployeeService employeeService;
    private final PhoneNumberService numberService;
    private final UserService userService;
    //private final ExportPDF exportPDF;

    public Optional<MobilePhone> findById(long id){
        return mobilePhoneRepo.findById(id);
    }

    public List<MobilePhone> findAll() {
        return mobilePhoneRepo.findAll();
    }

    public void save(MobilePhone mobilePhone) {
        if(mobilePhone.getDemage()==null){
            mobilePhone.setDemage(false);
        }
        mobilePhoneRepo.save(mobilePhone);
    }

    public List<MobilePhone> search(String matter){
        List<MobilePhone> mobilePhoneList=null;
        if(mobilePhoneRepo.findAllByModelIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByModelIsLike(matter);
        } else if(mobilePhoneRepo.findAllByIMEIIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByIMEIIsLike(matter);
        } else if(mobilePhoneRepo.findAllByIMEI2IsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByIMEI2IsLike(matter);
        }else if(mobilePhoneRepo.findAllByMarkIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByMarkIsLike(matter);
        }else if(mobilePhoneRepo.findAllByMACIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByMACIsLike(matter);
        } else if(mobilePhoneRepo.findAllByPhoneNumber_NumberIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByPhoneNumber_NumberIsLike(matter);
        }  else if( mobilePhoneRepo.findAllBySerialNumberIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllBySerialNumberIsLike(matter);
        }  else if(mobilePhoneRepo.findAllByEmployee_LastnameIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByEmployee_LastnameIsLike(matter);
        } else if(mobilePhoneRepo.findAllBySerialNumberIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllBySerialNumberIsLike(matter);
        } else if(mobilePhoneRepo.findAllByEmployee_LastnameIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo.findAllByEmployee_FirstnameIsLike(matter);
        } else if(mobilePhoneRepo. findAllByEmployee_Department_NameOfDepartmentIsLike(matter).size()!=0){
            mobilePhoneList=mobilePhoneRepo. findAllByEmployee_Department_NameOfDepartmentIsLike(matter);
        }

        return mobilePhoneList;
    }

    public void deleteMobilePhone(long id) {
        Optional<MobilePhone> mobilePhone = mobilePhoneRepo.findById(id);
        mobilePhoneRepo.delete(mobilePhone.get());
    }

}
