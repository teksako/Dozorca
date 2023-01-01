package com.selt.repository;

import com.selt.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepo extends JpaRepository<Office, Long> {


}
