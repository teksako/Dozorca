package com.selt.repository;

import com.selt.model.MobilePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobilePhoneRepo extends JpaRepository<MobilePhone, Long> {
    List<MobilePhone> findAllByModelIsLike(String model);
    List<MobilePhone> findAllByIMEIIsLike(String imei);
    List<MobilePhone> findAllByIMEI2IsLike(String imei2);
    List<MobilePhone> findAllByMarkIsLike(String mark);
    List<MobilePhone> findAllByMACIsLike(String mac);
    List<MobilePhone> findAllByPhoneNumber_NumberIsLike(String number);
    List<MobilePhone> findAllByEmployee_FirstnameIsLike(String number);
    List<MobilePhone> findAllByEmployee_LastnameIsLike(String number);
    List<MobilePhone> findAllByEmployee_Department_NameOfDepartmentIsLike(String department);


}
