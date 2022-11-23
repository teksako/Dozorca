package com.selt.controler;


import com.selt.model.Location;
import com.selt.model.Temp;
import com.selt.repository.LocationRepo;
import com.selt.service.LocationService;
import com.selt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    private final UserService userService;
    private final LocationRepo locationRepo;


    @GetMapping({"/list-locations"})
    public ModelAndView getAllLocation() {
        ModelAndView model = new ModelAndView("/list-locations");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("locationList", locationService.findAll());
        model.addObject("id", userService.findUserByUsername().getId());
        return model;
    }


    @PostMapping({"/saveLocation"})
    public String saveLocation(@ModelAttribute Location location) {
        locationService.save(location);
        return "redirect:list-locations";
    }

    @GetMapping({"/addLocationForm"})
    public ModelAndView addLocationForm() {
        ModelAndView model = new ModelAndView("add-location-form");
        Location location = new Location();
        model.addObject("location", location);
        return getModelAndView(model);
    }

    @GetMapping({"/showUpdateLocationForm"})
    public ModelAndView showUpdateLocationForm(@RequestParam Long locationId) {
        ModelAndView model = new ModelAndView("add-location-form");
        Location location = locationRepo.findById(locationId).get();
        model.addObject("location", location);
        return getModelAndView(model);
    }

    @PostMapping({"/list-locations"})
    public void searchDepartments(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("locationList", locationService.findAllByNameOfLocationIsLike(mattern));
        getAllLocation();
    }

    @NotNull
    private ModelAndView getModelAndView(ModelAndView model) {
        model.addObject("username", userService.findUserByUsername().getFullname());
//        List<Location> locationList = locationService.findAll();
//        model.addObject("locations", locationList);
        return model;
    }

    @GetMapping({"/deleteLocation/{id}"})
    public String deleteLocation(@PathVariable(value = "id") long id) {
        locationService.deleteLocation(id);
        getAllLocation();
        return "redirect:/list-departments";
    }
}


