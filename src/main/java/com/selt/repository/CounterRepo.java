package com.selt.repository;

import com.selt.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CounterRepo extends JpaRepository<Counter, Long> {
    List<Counter> findAllByPrinter_idIsLike(long id);
    List<Counter> findAllByPrinter_idIsLikeAndDateIsBetween(long id,LocalDate start, LocalDate end);
    Counter findByPrinter_IdIsLikeAndDateIsLike(long id, LocalDate date);
}
