package com.selt.controler;

import com.selt.model.Configuration;
import com.selt.model.Toner;
import com.selt.repository.ConfigRepo;
import com.selt.service.ConfigService;
import com.selt.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Data
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;
    private final ConfigRepo configRepo;
    private final UserService userService;

    @GetMapping({"/config-form"})
    public ModelAndView configForm() {
        ModelAndView model = new ModelAndView("config-form");
        Configuration configuration=configRepo.findById(1l).get();
        model.addObject("config", configuration);
        return getModelAndView(model);
    }
    @PostMapping({"/saveConfig"})
    public String saveConfig(@ModelAttribute Configuration configuration) {
        configService.save(configuration);

        return "redirect:list-toners";
    }

    @NotNull
    private ModelAndView getModelAndView(ModelAndView model) {
        model.addObject("username", userService.findUserByUsername().getFullname());
//        List<Location> locationList = locationService.findAll();
//        model.addObject("locations", locationList);
        return model;
    }
}
