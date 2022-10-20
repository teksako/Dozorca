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

    public List<OID> findAllByOidProducent(String producent){

        return oidRepo.findAllByOidProducentIsLike(producent);
    }


    public List<OID> findAllByPrinterModel(String prinerModel, List<OID> oidList){

        List<OID> finalOidList=new ArrayList<>();
        for (OID oid: oidList) {
            for (String model:oid.getPrinterModel())
                  {
                if(prinerModel.contains(model)){
                    finalOidList.add(oid);
                }
            }

        }
        return finalOidList;
    }
//    public List<OID> findAllByPrinterModel(Printer printer) {
//
//        List<OID> oidList = new ArrayList<>();
//        if (printer.getModel().contains("454") || printer.getModel().contains("284") || printer.getModel().contains("360") || printer.getModel().contains("253")) {
//            for (OID oid : findAllByOidProducent(printer.getManufacturer())) {
//                if (oid.getOidName().contains("Total Counter") || oid.getOidName().contains("Magenta Toner Level") || oid.getOidName().contains("Cyan Toner Level") || oid.getOidName().contains("Black Toner Level") || oid.getOidName().contains("Yellow Toner Level")) {
//                    oidList.add(oid);
//                }
//            }
//        }else if(printer.getModel().contains("3300") || printer.getModel().contains("4700")) {
//            for (OID oid : findAllByOidProducent(printer.getManufacturer())) {
//                if (oid.getOidName().contains("Total Counter") || oid.getOidName().contains("Actual Toner Capacity") || oid.getOidName().contains("Max Toner Capacity")||oid.getOidName().contains("Actual Drum Condition")||oid.getOidName().contains("Max Drum Condition")) {
//                    oidList.add(oid);
//                }
//            }
//        }else {
//            for (OID oid : findAllByOidProducent(printer.getManufacturer())) {
//                if (oid.getOidName().contains("Total Counter")){
//                    oidList.add(oid);
//                }
//            }
//        }
//
//            return oidList;
//    }
}
