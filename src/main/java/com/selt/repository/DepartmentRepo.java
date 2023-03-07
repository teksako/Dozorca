package com.selt.repository;

import com.selt.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {

    List<Department> findAllByNameOfDepartmentIsLike(String name);
    List<Department> findAllByOrderByNameOfDepartmentAsc();
}
