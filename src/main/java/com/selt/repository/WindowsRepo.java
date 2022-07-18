package com.selt.repository;

import com.selt.model.Windows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WindowsRepo extends JpaRepository<Windows, Long> {
}
