package com.selt.service;

import com.selt.model.Employee;
import com.selt.repository.EmployeeRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class EmployeeService {


    private final EmployeeRepo employeeRepo;
    private final DepartmentService departmentService;
    public List<Employee> findAll(){
        return cleanBlankEmployeer(employeeRepo.findAll());
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

    public List<Employee> cleanBlankEmployeer(List<Employee> employeeList) {
        List<Employee> employeeList1 = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (!employee.getFirstname().equals("-")) {
                employeeList1.add(employee);
            }
        }
        return employeeList1;
    }

    public List<Employee> findBlankEmployeer(){
        List<Employee> employeeList = new ArrayList<>();
        for (Employee employee : employeeRepo.findAll()) {
            if (employee.getFirstname().equals("-")) {
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    public List<Employee> findAllByLastnameIsLike(String name){
        return employeeRepo.findAllByLastnameIsLike(name);
    }

    public void save(Employee employee) {
//        if(employee.getDepartment()==null){
//            employee.setDepartment(departmentService.findBlankDepartment().get(0));
//        }
        employeeRepo.save(employee);
        //return "Dodano pracownika: " +employee;
    }


}
