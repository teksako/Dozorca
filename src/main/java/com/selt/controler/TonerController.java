package com.selt.controler;

import com.selt.model.Location;
import com.selt.model.Toner;
import com.selt.repository.TonerRepo;
import com.selt.service.MagazineService;
import com.selt.service.TonerService;
import com.selt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TonerController {
    private final TonerService tonerService;
    private final UserService userService;
    private final TonerRepo tonerRepo;
    private final MagazineService magazineService;
    @GetMapping({"/list-toners"})
    public ModelAndView getAllToner() {
        ModelAndView model = new ModelAndView("/list-toners");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("tonerList", tonerService.findAll());
        model.addObject("id", userService.findUserByUsername().getId());
        return model;
    }




    @PostMapping({"/saveToner"})
    public String saveToner(@ModelAttribute Toner toner) {
        tonerService.save(toner);
        magazineService.save(toner);
        return "redirect:list-toners";
    }

    @GetMapping({"/addTonerForm"})
    public ModelAndView addTonerForm() {
        ModelAndView model = new ModelAndView("add-toner-form");
        Toner toner = new Toner();
        model.addObject("toner", toner);
        return getModelAndView(model);
    }

    @GetMapping({"/showUpdateTonerForm"})
    public ModelAndView showUpdateTonerForm(@RequestParam Long tonerId) {
        ModelAndView model = new ModelAndView("add-toner-form");
        Toner toner = tonerRepo.findById(tonerId).get();
        model.addObject("toner", toner);
        return getModelAndView(model);
    }

    @NotNull
    private ModelAndView getModelAndView(ModelAndView model) {
        model.addObject("username", userService.findUserByUsername().getFullname());
//        List<Location> locationList = locationService.findAll();
//        model.addObject("locations", locationList);
        return model;
    }

    @GetMapping({"/deleteToner/{id}"})
    public String deleteLocation(@PathVariable(value = "id") long id) {
        tonerService.deleteToner(id);
        getAllToner();
        return "redirect:/list-toners";
    }
    @GetMapping({"/Toner"})
    public String tonerPage(Model model) {
        model.addAttribute("toner", new Toner());
        List<Toner> toners= tonerService.findAll();
        model.addAttribute("tonerList", toners);
        return "/Toner";
    }

//    @PostMapping({"/addToner"})
//    public String saveToner(@ModelAttribute("toner") Toner toner) {
//        tonerService.save(toner);
//
//        return "/Toner";
//    }


//    @GetMapping({"/deleteToner"})
//    public String deleteTonerPage(Model model) {
//        model.addAttribute("toner", new Toner());
//        List<Toner> toners= tonerService.findAll();
//        model.addAttribute("tonerList", toners);
//        return "/deleteToner";
//    }
    @PostMapping({"/deleteToner"})
    public String deleteToner(@ModelAttribute("toner") Toner toner){
        tonerService.delete(toner);

        return "/Toner";
    }

//    @GetMapping({"/updateToner"})
//    public String updateTonerPage(Model model) {
//        model.addAttribute("toner", new Toner());
//        List<Toner> toners= tonerService.findAll();
//        model.addAttribute("tonerList", toners);
//        return "/updateToner";
//    }
    @PostMapping({"/updateToner"})
    public String saveUpdateToner(@ModelAttribute("toner") Toner toner,Model model){
        tonerService.update(toner);
        tonerPage(model);
        return "/Toner";
    }
}
