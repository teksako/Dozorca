package com.selt.service;

import com.selt.model.OID;
import com.selt.model.Printer;
import com.selt.repository.OIDRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OIDService {

    @Autowired
    private final OIDRepo oidRepo;

    public OIDService(OIDRepo oidRepo) {
        this.oidRepo = oidRepo;
    }

    public List<OID> findAllByOidProducent(String producent) {

        return oidRepo.findAllByOidProducent(producent);
    }

    public List<OID> findAllByPrinterModel(Printer printer) {

        List<OID> oidList = new ArrayList<>();
        if (printer.getModel().contains("454") || printer.getModel().contains("284") || printer.getModel().contains("360") || printer.getModel().contains("253")) {
            for (OID oid : findAllByOidProducent(printer.getManufacturer())) {
                if (oid.getOidName().contains("Total Counter") || oid.getOidName().contains("Magenta Toner Level") || oid.getOidName().contains("Cyan Toner Level") || oid.getOidName().contains("Black Toner Level") || oid.getOidName().contains("Yellow Toner Level")) {
                    oidList.add(oid);
                }
            }
        }else{
            for (OID oid : findAllByOidProducent(printer.getManufacturer())) {
                if (oid.getOidName().contains("Total Counter") || oid.getOidName().contains("Actual Toner Capacity") || oid.getOidName().contains("Max Toner Capacity")) {
                    oidList.add(oid);
                }
            }
        }
            return oidList;
    }
}
