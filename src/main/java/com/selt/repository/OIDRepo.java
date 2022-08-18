package com.selt.repository;

import com.selt.model.OID;
import org.ietf.jgss.Oid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OIDRepo extends JpaRepository<OID, Long> {
List<OID> findAllByOidName(String oidName);
List<OID> findAllByOidProducent(String producent);
OID findOIDById(Long id);
OID findOIDByoidProducentAndOidName(String producent, String oidName);

}

