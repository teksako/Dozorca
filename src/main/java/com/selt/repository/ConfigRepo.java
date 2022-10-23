package com.selt.repository;

import com.selt.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ConfigRepo extends JpaRepository<Configuration, Long> {
}
