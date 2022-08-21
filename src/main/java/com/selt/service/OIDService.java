package com.selt.service;

import com.selt.model.OID;
import com.selt.repository.OIDRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OIDService {

    @Autowired
    private final OIDRepo oidRepo;

    public OIDService(OIDRepo oidRepo) {
        this.oidRepo = oidRepo;
    }

    public List<OID> findAllByOidProducent(String producent){

        return oidRepo.findAllByOidProducent(producent);
    }
}
