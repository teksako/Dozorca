package com.selt.repository;

import com.selt.model.Raport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RaportRepo extends JpaRepository<Raport, Long> {
    List<Raport> findAllByDateIsBetween(LocalDate start, LocalDate end);
    List<Raport> findAllByPrinterIsLike(String search);
    List<Raport> findAllByTonerIsLike(String search);
    List<Raport> findAllByDepartmentIsLike(String search);
    @Query("SELECT sum(e.count) from Raport e")
    long count();

//    @Query("SELECT SUM(m.countOfDays) FROM Holiday m where m.accepted ='AKCEPTACJA' and  m.actualLoggedUser=?1 ")
//    Long allDays(String owner);


}
