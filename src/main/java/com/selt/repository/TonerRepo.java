package com.selt.repository;

import com.selt.model.Toner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TonerRepo extends JpaRepository<Toner, Long> {
    List<Toner> findAllByTonerNameIsLike(String name);
}
