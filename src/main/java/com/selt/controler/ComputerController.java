package com.selt.controler;

import com.selt.model.Computer;
import com.selt.model.Temp;
import com.selt.repository.MobilePhoneRepo;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import com.selt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor


public class ComputerController {

    private final EmployeeService employeeService;
    private final OfficeService officeService;
    private final ComputerService computerService;
    private final UserService userService;

    Temp temp = new Temp();

    @GetMapping({"/addComputerForm"})
    public ModelAndView addComputerForm() {
        ModelAndView model = new ModelAndView("add-computer-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("officeKeyList", officeService.findAll());
        model.addObject("computer", new Computer());
        model.addObject("employeesList", employeeService.findAll());
        return model;
    }

    @GetMapping({"/showUpdateComputerForm"})
    public ModelAndView showUpdateComputerForm(@RequestParam Long computerId) {
        ModelAndView model = new ModelAndView("add-computer-form");
        model.addObject("employeesList", employeeService.findAll());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("computer", computerService.getComputerRepo().findById(computerId).get());
        return model;
    }


    @PostMapping({"/saveComputer"})
    public String savecomputer(@ModelAttribute("computer") Computer computer) {
        computerService.save(computer);
        return "redirect:/list-computers";
    }

    @GetMapping({"list-computers"})
    public ModelAndView getAllComputers() {
        ModelAndView model = new ModelAndView("list-computers");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("computerList", computerService.findAll());
        return model;
    }

    @GetMapping({"/deleteComputer/{id}"})
    public String deleteComputer(@PathVariable(value = "id") long id) {
        computerService.delete(id);
        getAllComputers();
        return "redirect:/list-computers";
    }

    @GetMapping({"/wakeUp/{id}"})
    public String wakeUp(@PathVariable(value = "id") long id) {
        computerService.wakeUp(id);
        //getAllComputers();
        return "redirect:/list-computers";
    }



    @PostMapping({"/addComputer"})
    public String saveComputer(@ModelAttribute("computer") Computer computer) {

        computerService.save(computer);
        return "/index";
    }
}
