package com.selt.controler;

import com.selt.model.Department;
import com.selt.model.Employee;
import com.selt.model.Temp;
import com.selt.repository.EmployeeRepo;
import com.selt.service.DepartmentService;
import com.selt.service.EmployeeService;
import com.selt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final UserService userService;
    public final EmployeeRepo employeeRepo;

    @GetMapping({"list-employees"})
    public ModelAndView getAllEmployees() {
        ModelAndView model = new ModelAndView("list-employees");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());

        for (Employee employee : employeeService.findAll()) {
            if (employee.getDepartment() == null) {
                employee.setDepartment(departmentService.getDepartmentRepo().getById(1l));
            }

        }
         model.addObject("employeesList", employeeService.findAll());
        return model;
    }

    @PostMapping({"/saveEmployee"})
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeService.save(employee);
        return "redirect:/list-employees";
    }


    @PostMapping({"/list-employees"})
    public void searchEmployee(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("employeeList", employeeService.findAllByLastnameIsLike(mattern));
        getAllEmployees();
    }

    @GetMapping({"/addEmployeeForm"})
    public ModelAndView addEmployee() {
        ModelAndView model = new ModelAndView("add-employee-form");
        List<Department> departmentList = departmentService.findAll();
        model.addObject("departmentList", departmentList);
        model.addObject("employee", new Employee());
        return model;
    }

    @GetMapping({"/showUpdateEmployeeForm"})
    public ModelAndView showUpdateEmployeeForm(@RequestParam Long employeeId) {
        ModelAndView model = new ModelAndView("add-employee-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        Employee employee = employeeRepo.findById(employeeId).get();
        model.addObject("departmentList", departmentService.findAll());
        model.addObject("employee", employee);
        return model;
    }

    @GetMapping({"/deleteEmployee/{id}"})
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        employeeService.deleteEmployee(id);
        getAllEmployees();
        return "redirect:/list-employees";
    }

}
