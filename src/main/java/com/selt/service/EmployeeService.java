package com.selt.service;

import com.selt.model.Department;
import com.selt.model.Employee;
import com.selt.repository.EmployeeRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteEmployee(long id) {
        Optional<Employee> employee = employeeRepo.findById(id);
        employeeRepo.delete(employee.get());
    }

    public List<Employee> findAllByFirstnameIsLike(String name){
        return employeeRepo.findAllByFirstnameIsLike(name);
    }

    public List<Employee> findAllByLastnameIsLike(String name){
        return employeeRepo.findAllByLastnameIsLike(name);
    }

    public void save(Employee employee) {
        employeeRepo.save(employee);
        //return "Dodano pracownika: " +employee;
    }


}
