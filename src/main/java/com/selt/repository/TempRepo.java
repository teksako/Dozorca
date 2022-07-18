package com.selt.repository;

import com.selt.model.Temp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempRepo extends JpaRepository<Temp, Long> {
}
