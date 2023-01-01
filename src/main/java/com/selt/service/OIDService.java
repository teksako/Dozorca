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

}
