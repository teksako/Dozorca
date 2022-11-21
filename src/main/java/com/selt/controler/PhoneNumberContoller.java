package com.selt.controler;

import com.selt.model.PhoneNumber;
import com.selt.service.PhoneNumberService;
import com.selt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PhoneNumberContoller {

    private final PhoneNumberService numberService;
    private final UserService userService;

    @GetMapping({"/list-phoneNumbers"})
    public ModelAndView getAllNumbers(){
        ModelAndView model = new ModelAndView("list-phoneNumbers");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("numberList", numberService.findAll());
        return model;
    }


    @PostMapping({"saveNumber"})
    public String saveNumber(@ModelAttribute PhoneNumber number){
        numberService.save(number);
        return "redirect:/list-phoneNumbers";
    }

    @GetMapping({"/addPhoneNumberForm"})
    public ModelAndView addNumberForm(){
        ModelAndView model = new ModelAndView("add-phoneNumber-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("number", new PhoneNumber());
        return model;
    }
}
