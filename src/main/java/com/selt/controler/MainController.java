package com.selt.controler;

import com.selt.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@NoArgsConstructor
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/index"})
    public String mainPage(Model model){
       model.addAttribute("username",  userService.findUserByUsername().getFullname());

        return "index";
    }

    @GetMapping({"/index2"})
    public String mainPage2(){
        return "witaj swiecie2!";
    }

}
