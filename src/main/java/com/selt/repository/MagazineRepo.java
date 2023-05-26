package com.selt.repository;

import com.selt.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagazineRepo extends JpaRepository<Magazine, Long> {
    List<Magazine> findAllMagazinesByToner_TonerNameIsLike(String temp);
    Magazine findByTonerId(long id);
    //List<Magazine> findAllByOrderByToner_TonerNameAsc();
}
