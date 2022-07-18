package com.selt.service;

import com.selt.model.Department;
import com.selt.model.Location;
import com.selt.model.Printer;
import com.selt.repository.DepartmentRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final LocationService locationService;

    public void save(Department department) {
        departmentRepo.save(department);
    }

    public void delete(Department department) {
        departmentRepo.delete(department);
    }

    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

//    public List<Location> findActualUse(long id) {
//        Optional<Department> department = departmentRepo.findById(id);
//        List<Location> actualList = department.get().getLocations();
//        List<Location> findAll = locationService.findAll();
//        List<Location> finallyList = new ArrayList<>();
//        for (Location list : findAll) {
//            for (Location list2 : actualList) {
//                if(actualList.size()==0){
//                    return findAll;
//                }
//                else if (!list.equals(list2)) {
//                    finallyList.add(list);
//                }
//            }
//            finallyList.remove(list);
//        }
//        return findAll;
//    }

    public void deleteDepartment(long id) {
        Optional<Department> department = departmentRepo.findById(id);
        departmentRepo.delete(department.get());
    }

    public List<Department> findAllByNameOfDepartmentIsLike(String name) {
        return departmentRepo.findAllByNameOfDepartmentIsLike(name);
    }
}
