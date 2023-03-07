package com.selt.repository;

import com.selt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
List<Employee> findAllByFirstnameIsLike(String name);
List<Employee> findAllByLastnameIsLike(String name);
List<Employee> findAllByDepartment_NameOfDepartmentIsLike(String department);
List<Employee> findAllByWorkplaceIsLike(String workplace);
List<Employee> findAllByOrderByLastnameAsc();

}
