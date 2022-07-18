package com.selt.repository;

import com.selt.model.Printer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrinterRepo extends JpaRepository<Printer, Long> {

   List<Printer> findAllByTonerList_TonerNameIsLike(String toner);
   List<Printer> findAllByModelIsLike(String model);
   List<Printer> findAllByManufacturerIsLike(String manufacturer);
   List<Printer> findAllByIPAdressIsLike(String ip);
   List<Printer> findAllByMACAdressIsLike(String mac);
   List<Printer> findAllByDepartment_NameOfDepartmentIsLike(String departmenrt);
   List<Printer> findAllBySerialNumberIsLike(String serialNumber);
   List<Printer> findAllByInventoryNumberIsLike(String inventorynumber);
   List<Printer> findAllByUserIsLike(String user);


}
