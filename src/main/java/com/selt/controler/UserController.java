package com.selt.controler;

import com.selt.model.Department;
import com.selt.model.Temp;
import com.selt.model.User;
import com.selt.model.UserRole;
import com.selt.repository.RoleRepo;
import com.selt.repository.UserRepo;
import com.selt.service.RoleServis;
import com.selt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final RoleServis roleServis;


    @GetMapping({"/list-users"})
    public ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView("list-users");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("userList", userService.findAll());
        model.addObject("id", userService.findUserByUsername().getId());
        return model;
    }

    @PostMapping({"/saveUser"})
    public String saveUser(User user, String password, Model model) {
        model.addAttribute("password", password);
        userService.save(user, password);
        getAllUsers();
        return "redirect:/list-users";
    }

    @PostMapping({"/saveUpdateUser"})
    public String saveUpdateUser(User user, String password, Model model) {
        model.addAttribute("password", password);
        userService.saveUpdate(user, password);
        getAllUsers();
        return "redirect:/list-users";
    }

    @GetMapping("/addUserForm")
    public ModelAndView addUserForm() {
        ModelAndView model = new ModelAndView("add-user-form");
        User user = new User();
        model.addObject("user", user);
        return getModelAndView(model);

    }

    @GetMapping({"/showUpdateUserForm"})
    public ModelAndView showUpdateUserForm(@RequestParam Long userId) {
        ModelAndView model = new ModelAndView("update-user-form");
        User user = userRepo.findById(userId).get();
        model.addObject("user", user);
        return getModelAndView(model);
    }

    @NotNull
    private ModelAndView getModelAndView(ModelAndView model) {
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("roleList", roleServis.findAll());
        return model;
    }

    @GetMapping({"/deleteUser/{id}"})
    public String deletePrinter(@PathVariable(value = "id") long id) {
        userService.deleteUser(id);
        getAllUsers();
        return "redirect:/list-users";
    }

    @PostMapping({"/changePassword"})
    public String changePasword(String password, Model model) {
        model.addAttribute("password", password);

        userService.changePassword(password);
        getAllUsers();
        return "add-user-form";
    }

    @PostMapping({"/resetPassword"})
    public String resetPasword(User user, String password, Model model) {
        model.addAttribute("password", password);

        userService.resetPasswordByAdmin(user,password);
        getAllUsers();
        return "add-user-form";
    }
}
