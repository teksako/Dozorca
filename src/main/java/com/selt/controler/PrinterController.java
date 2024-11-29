package com.selt.controler;

import com.selt.model.*;
import com.selt.repository.MobilePhoneRepo;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import com.selt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrinterController {

    private final DepartmentService departmentService;

    private final TonerService tonerService;

    private final PrinterService printerService;
    private final UserService userService;
    private final PrinterRepo printerRepo;
    private final OIDRepo oidRepo;
    private final CounterService counterService;


    Temp temp = new Temp();

    @GetMapping({"list-printers"})
    public ModelAndView getAllPrinters() {
        ModelAndView model = new ModelAndView("list-printers");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("printerList", printerService.findAll());
        return model;
    }


    @GetMapping({"/showInfoForm"})
    public ModelAndView showInfoForm(@RequestParam long id, String allert) {
        ModelAndView model = new ModelAndView("info-printer-form");
        Temp temp = new Temp();
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("printer", printerService.findById(id).get().getManufacturer() + " " + printerRepo.findById(id).get().getModel() + " w dziale " + printerRepo.findById(id).get().getDepartment().getNameOfDepartment());
        model.addObject("printerIP", printerService.findById(id).get().getIPAdress());
        model.addObject("counter", counterService.getActualCounter(id));
        model.addObject("printerId", id);
        model.addObject("temp", temp);
        model.addObject("tonerList", printerService.findAlltoner(id));
        model.addObject("allert", allert);
        model.addObject("serviceCounter", printerService.validateServiceCounter(id));
        return model;
    }

    @GetMapping("/resetCounter")
    public ModelAndView reset(@RequestParam Long printerId) {

        printerService.resetServiceCounter(printerId);
        String allert = "Wyzerowano licznik!";
        return showInfoForm(printerId, allert);

    }


    @GetMapping({"/addPrinterForm"})
    public ModelAndView addPrinterForm() {
        ModelAndView model = new ModelAndView("add-printers-form");
        Printer printer = new Printer();
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("printer", printer);
        List<Department> departmentList = departmentService.findAll();
        List<OID> oidList = oidRepo.findAll();
        List<Toner> tonerList = tonerService.findAll();
        model.addObject("oidlist", oidList);
        model.addObject("toners", tonerList);
        model.addObject("departments", departmentList);

        return model;

    }

    @PostMapping({"/savePrinter"})
    public String savePrinter(@ModelAttribute Printer printer) {
        printerService.save(printer);
        return "redirect:/list-printers";
    }

    @GetMapping({"/deletePrinter/{id}"})
    public String deletePrinter(@PathVariable(value = "id") long id) {
        printerService.deletePrinter(id);
        getAllPrinters();
        return "redirect:/list-printers";
    }


    @PostMapping({"/list-printers"})
    public void searchPrinters(@ModelAttribute("temp") Temp temp, Model model) {

        List<Printer> printerList = null;
        String mattern = '%' + temp.getTempString() + '%';

        if (temp.getTempString() == null) {
            if (printerList == null) {
                model.addAttribute("allert", "Brak danych!");
            }
            printerList = printerService.findAll();

        } else {
            if (printerService.findAllByModelIsLike(mattern).size() != 0) {
                printerList = printerService.findAllByModelIsLike(mattern);

            } else if (printerRepo.findAllByManufacturerIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByManufacturerIsLike(mattern);
            } else if (printerRepo.findAllByDepartment_NameOfDepartmentIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByDepartment_NameOfDepartmentIsLike(mattern);
            } else if (printerRepo.findAllByMACAdressIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByMACAdressIsLike(mattern);
            } else if (printerRepo.findAllBySerialNumberIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllBySerialNumberIsLike(mattern);
            } else if (printerRepo.findAllByUserIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByUserIsLike(mattern);
            } else if (printerRepo.findAllByInventoryNumberIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByInventoryNumberIsLike(mattern);
            } else if (printerRepo.findAllByDepartment_NameOfDepartmentIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByDepartment_NameOfDepartmentIsLike(mattern);
            } else if (printerRepo.findAllByTonerList_TonerNameIsLike(mattern).size() != 0) {
                printerList = printerRepo.findAllByTonerList_TonerNameIsLike(mattern);
            } else {
                printerList = printerRepo.findAllByIPAdressIsLike(mattern);
            }

        }
        model.addAttribute("printerList", printerList);
        model.addAttribute("username", userService.findUserByUsername().getFullname());
        getAllPrinters();

    }

    @GetMapping({"/showUpdateForm"})
    public ModelAndView showUpdateForm(@RequestParam Long printerId) {
        ModelAndView model = new ModelAndView("add-printers-form");
        Printer printer = printerRepo.findById(printerId).get();
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("printer", printer);
        model.addObject("oidlist", oidRepo.findAll());
        model.addObject("toners", tonerService.findAll());
        model.addObject("departments", departmentService.findAll());

        return model;
    }

}
