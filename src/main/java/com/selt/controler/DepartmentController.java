package com.selt.controler;

import com.selt.model.Department;
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

@Controller
@Data
public class DepartmentController {

    private final DepartmentService departmentService;
    private final LocationService locationService;
    private final UserService userService;
    private final DepartmentRepo departmentRepo;

    @GetMapping({"/list-departments"})
    public ModelAndView getAllDepartments() {
        ModelAndView model = new ModelAndView("list-departments");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("departmentList", departmentService.findAll());
        model.addObject("id", userService.findUserByUsername().getId());
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

//
//    @GetMapping({"/showUpdateDepartmentForm"})
//    public ModelAndView showUpdateDepartmentForm(@RequestParam Long departmentId) {
//        ModelAndView model = new ModelAndView("add-department-form");
//        Department department = departmentRepo.findById(departmentId).get();
//        //Printer printer = printerService.findById(printerId).get();
//        model.addObject("department", department);
//        return getModelAndView(model, departmentId);
//    }
//
//    @NotNull
//    private ModelAndView getModelAndView(ModelAndView model, long id) {
//        model.addObject("username", userService.findUserByUsername().getFullname());
////        List<Location> locationList = locationService.findAll();
////        model.addObject("locations", locationList);
//        model.addObject("locationList", departmentService.findActualUse(id));
//        return model;
//    }

    @GetMapping({"/deleteDepartment/{id}"})
    public String deletePrinter(@PathVariable(value = "id") long id) {
        departmentService.deleteDepartment(id);
        getAllDepartments();
        return "redirect:/list-departments";
    }
    //    ----------STARA WERSJA---------------
//    @GetMapping({"/addDepartment"})
//    public String departmentPage(Model model){
//        model.addAttribute("department", new Department());
//        List<Location> locationList = locationService.findAll();
//        List<Department> departmentList=departmentService.findAll();
//        model.addAttribute("departmentList", departmentList);
//        model.addAttribute("locationSList", locationList);
//        return "add-department-form";
//    }


}
