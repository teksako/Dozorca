package com.selt.repository;

import com.selt.model.OID;
import com.selt.model.Printer;
import org.ietf.jgss.Oid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OIDRepo extends JpaRepository<OID, Long> {
List<OID> findAllByOidName(String oidName);
List<OID> findAllByOidProducentIsLike(String producent);
List<OID> findAllByPrinterModelIsLike(String printerModel);
OID findOIDById(Long id);
OID findOIDByoidProducentAndOidName(String producent, String oidName);

}

