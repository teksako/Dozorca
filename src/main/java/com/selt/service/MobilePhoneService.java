package com.selt.service;

import com.itextpdf.text.DocumentException;
import com.selt.model.MobilePhone;
import com.selt.model.Temp;
import com.selt.repository.MobilePhoneRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class MobilePhoneService {


    private final MobilePhoneRepo mobilePhoneRepo;
    private final EmployeeService employeeService;
    private final PhoneNumberService numberService;
    private final MobilePhoneHistoryService mobilePhoneHistoryService;
    private final UserService userService;
    //private final ExportPDF exportPDF;

    public Optional<MobilePhone> findById(long id){
        return mobilePhoneRepo.findById(id);
    }

    public void releasePhone(Optional<MobilePhone> mobilePhone, Temp temp){

        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");
        try {


            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);
                mobilePhoneHistoryService.save(mobilePhone.get(), "WYDANIE", pdfName, LocalDate.parse(temp.getDate()));
                mobilePhone.get().setHasUser(true);
                save(mobilePhone.get());


            }

        } catch (StackOverflowError | IOException | DocumentException e) {
            System.out.println("Nie udało się, wszystkie nazwy są już zajetę!");
            //getAllPhones(message);

        }
    }


    public void getPhone(Optional<MobilePhone> mobilePhone, Temp temp){

        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");


        try {
            //String pdfName = mobilePhoneHistoryService.validatePdfName();

            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);

                mobilePhoneHistoryService.save(mobilePhone.get(), "ZDANIE", pdfName, LocalDate.parse(temp.getDate()));
                mobilePhone.get().setHasUser(false);
                mobilePhone.get().setEmployee(null);
                mobilePhone.get().setPhoneNumber(null);
                save(mobilePhone.get());
                //savePdf(mobilePhone.get());
            }


        } catch (StackOverflowError | IOException | DocumentException e) {
            System.out.println("Nie udało się, wszystkie nazwy są już zajetę!");
        }
    }


    public List<MobilePhone> findAll() {
        return mobilePhoneRepo.findAll();
    }

    public void save(MobilePhone mobilePhone) {
        if(mobilePhone.getDemage()==null){
            mobilePhone.setDemage(false);
        }
        if(mobilePhone.getHasUser()==null){
            mobilePhone.setHasUser(false);
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
