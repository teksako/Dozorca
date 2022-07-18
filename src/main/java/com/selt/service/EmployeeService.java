package com.selt.service;

import com.selt.model.Employee;
import com.selt.repository.EmployeeRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepo employeeRepo;

    public List<Employee> findAll(){
        return employeeRepo.findAll();
    }

    public void delete(Employee employee){
        employeeRepo.delete(employee);
    }


    public void save(Employee employee) {
        employeeRepo.save(employee);
        //return "Dodano pracownika: " +employee;
    }
}
