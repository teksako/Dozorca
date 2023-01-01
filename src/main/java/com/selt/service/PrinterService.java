package com.selt.service;

import com.selt.model.*;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import com.sun.jdi.VoidValue;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Data
@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class PrinterService {

    @Autowired
    private final PrinterRepo printerRepo;
    @Autowired
    private final OIDRepo oidRepo;
    @Autowired
    private final OIDService oidService;
    @Autowired
    private final DepartmentService departmentService;
    @Autowired
    private final TonerService tonerService;
    private final ConfigService configService;

    private static List<Printer> printerList = new ArrayList<>();

    public Optional<Printer> findById(Long id) {
        return printerRepo.findById(id);
    }

    public List<Printer> findAll() {
        return printerRepo.findAll();
    }



    public int randomNumber() {
        int min = 0;
        int max = 100;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public List<String> getIP() {
        List<Printer> printerList = printerRepo.findAll();
        List<String> IPAdress = new ArrayList<>();
        for (Printer printer : printerList) {
            String model = printer.getManufacturer();
            if (model.equals("Konica Minolta") && !printer.getIPAdress().equals("-")) {

                IPAdress.add(printer.getIPAdress());
            }
        }
        return IPAdress;
    }


    public List<Toner> findAlltoner(long id) {
        Optional<Printer> printer = printerRepo.findById(id);
        List<Toner> tonerList = printer.get().getTonerList();
        return tonerList;
    }

    public List<OID> findActualUse(long id) {
        Optional<Printer> printer = printerRepo.findById(id);
        List<OID> actualList = printer.get().getOid();
        List<OID> findAll = oidRepo.findAll();
        List<OID> finallyList = new ArrayList<>();
        for (OID list : findAll) {
            if (actualList.size() != 0) {
                for (OID list2 : actualList) {
                    if (list.equals(list2)) {
                        finallyList.add(list);
                    }
                }
                //finallyList.remove(list);
            } else {
                finallyList = findAll;
            }

        }
        return findAll;
    }


    public void resetServiceCounter(Long id) {
        Printer printer = printerRepo.getById(id);
        String oidValue = null;
        for (OID oid : printer.getOid()
        ) {
            if (oid.getOidName().equals("Total Counter")) {
                oidValue = oid.getOidValue();
            }

        }
        printer.setServiceCounter(SNMP4J.snmpGet(printer.getIPAdress(), "public", oidValue));
    }

    public String validateServiceCounter(long id) {
        Printer printer = printerRepo.getById(id);
        String oidValue = null;
        for (OID oid : printer.getOid()
        ) {
            if (oid.getOidName().equals("Total Counter")) {
                oidValue = oid.getOidValue();
            }

        }
        Long counte1 = (SNMP4J.snmpGet(printer.getIPAdress(), "public", oidValue));
        Long counter2 = printer.getServiceCounter();

        if (counte1 - counter2 < configService.getConfigRepo().getById(1l).getServiceCallcounter()) {
            return configService.getConfigRepo().getById(1l).getServiceCallcounter() - (counte1 - counter2) + " stron do przeglądu";
        } else {
            return "Wykonaj przegląd!";
        }


    }

    public Optional<Printer> findById(long id) {
        return printerRepo.findById(id);
    }


    public void reload() {
        for (Printer printer : printerRepo.findAll()
        ) {
            if (printer.getCollectCounter().equals(true)) {
                save(printer);
            }
        }

    }

    public void save(Printer printer) {

        printerRepo.save(printer);
    }

    public void deletePrinter(long id) {
        Optional<Printer> printer = printerRepo.findById(id);
        printerRepo.delete(printer.get());
    }

    public void editPrinter(long id) {
        Optional<Printer> printer1 = printerRepo.findById(id);
        printerRepo.save(printer1.get());
    }

    public List<Printer> findAllByModelIsLike(String model) {
        return printerRepo.findAllByModelIsLike(model);
    }

}
