package com.selt.repository;

import com.selt.model.Toner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TonerRepo extends JpaRepository<Toner, Long> {
}
