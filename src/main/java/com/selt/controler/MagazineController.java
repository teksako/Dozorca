package com.selt.controler;

import com.selt.model.*;
import com.selt.service.*;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;


@Controller
@Data
public class MagazineController {

    private final TonerService tonerService;
    private final MagazineService magazineService;
    private final PrinterService printerService;
    private final UserService userService;
    private final HardwareController hardwareController;
    private final RaportService raportService;


    @GetMapping({"/Magazine"})
    public void userPage(Model model) {
        model.addAttribute("temp", new Temp());
        model.addAttribute("magazine", new Magazine());
        model.addAttribute("printers", new Printer());
        model.addAttribute("username", userService.findUserByUsername().getFullname());
        List<Magazine> magazines = magazineService.findAll();
        model.addAttribute("tonerList", magazines);
        List<Printer> printerList = printerService.findAll();
        model.addAttribute("printer", printerList);
        model.addAttribute("id", userService.findUserByUsername().getId());

    }

    @PostMapping({"/addMagazine"})
    public String addToner(@ModelAttribute("magazine") Magazine magazine, Model model) {
        try {
            if (magazine.getCount() < 1l) {
                model.addAttribute("allert", "Ujemna liczba!");
            } else {
                magazineService.updateInventory(magazine, magazineService.getActualCount(magazine));
            }
        } catch (IllegalArgumentException | NullPointerException exception) {
            model.addAttribute("exception", "Wybierz toner!");
        }
        userPage(model);
        return "/Magazine";

    }


    @PostMapping({"/removeMagazine2"})
    public ModelAndView removeToner2(@ModelAttribute("temp") Temp temp) {
        Long printerId = temp.getId_2();
        Long tonerId = temp.getId_1();
        System.out.println("id toneru " + tonerId);
        Long count = temp.getRadio();
        System.out.println("ile chce: " + count);
        String allert = null;
        System.out.println("id drukarki: " + printerId);
        try {
            if (magazineService.magazineValidation(count, tonerId) == true) {
                magazineService.removeFromMagazine(tonerId, count);
                raportService.create(tonerId, count, printerId);

            } else {
                allert = "Nie masz tyle na stanie!";

            }
        } catch (NullPointerException exception) {
            allert = "Wybierz toner!";

        }

        return hardwareController.showInfoForm(printerId, allert);

    }


    @PostMapping({"/getMagazine"})
    public String showMagazine(@ModelAttribute("temp") Temp temp, Model model) {
        String matterm = '%' + temp.getTempString() + '%';
        List<Magazine> foundMagazines = magazineService.findAllMagazinesByToner_TonerNameIsLike(matterm);

        if (foundMagazines.size() == 0) {

            model.addAttribute("error", "Nie ma takiego toneru");
        }
        model.addAttribute("tonerLists", foundMagazines);
        userPage(model);
        return "/Magazine";
    }

    @ResponseBody
    @GetMapping({"/showMagazine"})
    public List<Magazine> getMagazine() {
        return magazineService.findAll();
    }

}

