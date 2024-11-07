package com.selt.repository;

import com.selt.model.Laptop;
import com.selt.model.MobilePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepo extends JpaRepository<Laptop, Long> {
    List<Laptop> findAllByModelIsLike(String model);
    List<Laptop> findAllByWindowsKeyIsLike(String windowsKey);
    List<Laptop> findAllByHostnameIsLike(String hostname);
    List<Laptop> findAllByManufacturerIsLike(String mark);
    List<Laptop> findAllByMACAdressWifiIsLike(String mac);
    List<Laptop> findAllBySerialNumberIsLike(String serial);
    List<Laptop> findAllByEmployee_FirstnameIsLike(String firstName);
    List<Laptop> findAllByEmployee_LastnameIsLike(String lastName);
    List<Laptop> findAllByEmployee_Department_NameOfDepartmentIsLike(String department);
    //List<Laptop> findAllByEmployee_Lastname(String lastname);
    List<Laptop> findAllByEmployee_IdIs(long id);
}
