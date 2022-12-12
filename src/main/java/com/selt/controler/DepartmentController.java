package com.selt.controler;

import com.selt.model.Department;
import com.selt.model.Location;
import com.selt.model.Temp;
import com.selt.repository.DepartmentRepo;
import com.selt.service.DepartmentService;
import com.selt.service.LocationService;
import com.selt.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Data
public class DepartmentController {

    private final DepartmentService departmentService;
    private final LocationService locationService;
    private final UserService userService;
    private final DepartmentRepo departmentRepo;
    private static Location location = new Location();
    static List<Location> locationList=new ArrayList<>();
    public static void setLocation(Location location) {
        DepartmentController.location = location;
        location.setNameOfLocation("-");

        locationList.add(location);
    }

    @GetMapping({"/list-departments"})
    public ModelAndView getAllDepartments() {
        ModelAndView model = new ModelAndView("list-departments");
        model.addObject("temp", new Temp());
        model.addObject("id", userService.findUserByUsername().getId());
        model.addObject("username", userService.findUserByUsername().getFullname());
        for (Department department: departmentService.findAll()) {
            if(department.getLocations().size()==0){
                department.setLocations(locationList);
            }
        }

        model.addObject("departmentList", departmentService.findAll());

        return model;
    }


    @PostMapping({"/saveDepartment"})
    public String saveDepartment(@ModelAttribute Department department) {
        departmentService.save(department);
        return "redirect:/list-departments";
    }

    @PostMapping({"/list-departments"})
    public void searchDepartments(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("departmentList", departmentService.findAllByNameOfDepartmentIsLike(mattern));
        getAllDepartments();
    }

    @GetMapping("/addDepartmentForm")
    public ModelAndView addPrinterForm() {
        ModelAndView model = new ModelAndView("add-department-form");
        Department department = new Department();
        model.addObject("locationList", locationService.findAll());
        model.addObject("department", department);
        return model;

    }
    @GetMapping({"/showUpdateDepartmentForm"})
    public ModelAndView showUpdateDepartmentForm(@RequestParam Long departmentId) {
        ModelAndView model = new ModelAndView("add-department-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        Department department = departmentRepo.findById(departmentId).get();
        model.addObject("locationList", locationService.findAll());
        model.addObject("department", department);
        return model;
    }



    @GetMapping({"/deleteDepartment/{id}"})
    public String deletePrinter(@PathVariable(value = "id") long id) {
        departmentService.deleteDepartment(id);
        getAllDepartments();
        return "redirect:/list-departments";
    }



}
