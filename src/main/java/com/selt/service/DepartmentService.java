package com.selt.service;

import com.selt.model.Department;
import com.selt.model.Employee;
import com.selt.model.Location;
import com.selt.model.Printer;
import com.selt.repository.DepartmentRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final LocationService locationService;

    public void save(Department department) {

        if(department.getLocations().isEmpty()){
            department.setLocations(locationService.findBlankLocation());
        }

        departmentRepo.save(department);
    }

    public void delete(Department department) {
        departmentRepo.delete(department);
    }

    public List<Department> findAll() {
        return validate(departmentRepo.findAll());
    }

    public List<Department> findBlankDepartment(){

        List<Department> departmentList = new ArrayList<>();
        for (Department department : departmentRepo.findAll()) {
            if (department.getNameOfDepartment().equals("-")) {
                departmentList.add(department);
            }
        }
        return departmentList;
    }

    public List<Department> validate(List<Department> departmentList) {
        List<Department> departmentList1 = new ArrayList<>();
        for (Department department : departmentList) {
            if (!department.getNameOfDepartment().equals("-")) {
                departmentList1.add(department);
            }
        }
        return departmentList1;
    }


    public void deleteDepartment(long id) {
        Optional<Department> department = departmentRepo.findById(id);
        departmentRepo.delete(department.get());
    }

    public List<Department> findAllByNameOfDepartmentIsLike(String name) {
        return validate(departmentRepo.findAllByNameOfDepartmentIsLike(name));
    }
}
