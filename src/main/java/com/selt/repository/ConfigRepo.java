package com.selt.repository;

import com.selt.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo extends JpaRepository<Configuration, Long> {
}
