package com.selt.controler;

import com.itextpdf.text.DocumentException;
import com.selt.model.*;
import com.selt.repository.MobilePhoneRepo;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import com.selt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller

@RequiredArgsConstructor

public class HardwareController {

    private final DepartmentService departmentService;
    private final LaptopService laptopService;
    private final EmployeeService employeeService;
    private final WindowsService windowsService;
    private final OfficeService officeService;
    private final ComputerService computerService;
    private final TonerService tonerService;
    private final MobilePhoneService mobilePhoneService;
    private final PhoneNumberService phoneNumberService;
    private final PrinterService printerService;
    private final UserService userService;
    private final PrinterRepo printerRepo;
    private final OIDRepo oidRepo;
    private final CounterService counterService;
    private final MobilePhoneRepo phoneRepo;
    private final MobilePhoneHistoryService mobilePhoneHistoryService;
    Temp temp = new Temp();



    @ResponseBody
    @GetMapping({"/showLaptops"})
    public List<Laptop> getLaptops() {
        return laptopService.findAll();
    }


    @GetMapping({"/showUserHardware"})
    public String getHardwares(Model model) {

        List<Computer> computerList = computerService.findAllByEmployee();
        List<Laptop> laptopList = laptopService.findAllByEmployee();
        model.addAttribute("computer", computerList);
        model.addAttribute("laptop", laptopList);
        return "/showUserHardware";
    }

    //---------------------OIDS-------------------------
    @PostMapping({"/saveOid"})
    public String savePrinter(@ModelAttribute OID oid) {
        oidRepo.save(oid);
        return "redirect:/list-printers";
    }

    //-------------------------PRINTERS----------------------------------
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


    //---------------------------END PRINTERS--------------------------------------------------

    //--------------------------START MOBILEPHONE--------------------------------------
    @GetMapping({"/list-phones"})
    public ModelAndView getAllPhones() {
        ModelAndView model = new ModelAndView("list-phones");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("phonesList", mobilePhoneService.findAll());

        return model;
    }

    @PostMapping({"/savePhone"})
    public String savePhone(@ModelAttribute("phone") MobilePhone mobilePhone)  {
        mobilePhoneService.save(mobilePhone);


        return "redirect:/list-phones";
    }

//    // @PostMapping(value = "/savePhone", produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<InputStreamResource> savePdf(MobilePhone mobilePhone) throws DocumentException, IOException {
//
////         ByteArrayInputStream bis = ExportPDF.protcol(mobilePhone, userService.findUserByUsername().getFullname(), "test");
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("Content-Disposition", "attachment;filename=test.pdf");
////        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
//
//    }


    @GetMapping({"/showPhoneInfoForm"})
    public ModelAndView showPhoneInfoForm(@RequestParam long id, String allert) {
        ModelAndView model = new ModelAndView("info-mobilePhone-form");
        Temp temp = new Temp();
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("historyList", mobilePhoneHistoryService.findAllByIMEI(mobilePhoneService.findById(id).get().getIMEI()));

        return model;
    }
    @PostMapping({"/list-phones"})
    public void searchPhones(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("phonesList", mobilePhoneService.search(mattern));
        getAllPhones();
    }

    @GetMapping({"/addPhoneForm"})
    public ModelAndView addPhonePage() {
        ModelAndView model = new ModelAndView("add-phone-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("phoneNumberList", phoneNumberService.findAll());
        model.addObject("phone", new MobilePhone());
        model.addObject("employeesList", employeeService.findByOrderByLastnameAsc());
        return model;
    }

    @GetMapping({"/showUpdatePhoneForm"})
    public ModelAndView showUpdatePhoneForm(@RequestParam Long phoneId) {
        ModelAndView model = new ModelAndView("add-phone-form");
        model.addObject("employeesList", employeeService.findAll());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("phoneNumberList", phoneNumberService.findByOrderByNumberAsc());
        model.addObject("phone", phoneRepo.findById(phoneId).get());
        return model;
    }


    @GetMapping({"/releasePhone/{id}"})
    public String releasePhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);

        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");

        if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
            ByteArrayInputStream bis = ExportPDF.protcol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp);
            mobilePhoneHistoryService.save(mobilePhone.get(), "WYDANIE");

        }
        getAllPhones();
        return "redirect:/list-phones";
    }

    @GetMapping({"/getPhone/{id}"})
    public String getPhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {


        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);


        if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
            ByteArrayInputStream bis = ExportPDF.protcol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp);
            mobilePhoneHistoryService.save(mobilePhone.get(), "ZDANIE");
            //savePdf(mobilePhone.get());
        }
        mobilePhone.get().setEmployee(null);
        mobilePhone.get().setPhoneNumber(null);
        savePhone(mobilePhone.get());
        getAllPhones();
        return "redirect:/list-phones";
    }

    @GetMapping({"/deletePhone/{id}"})
    public String deletePhone(@PathVariable(value = "id") long id) {
        mobilePhoneService.deleteMobilePhone(id);
        getAllPhones();
        return "redirect:/list-phones";
    }


//-------------------------END MOBILEPHONE---------------------------------

    @GetMapping({"/addLaptop"})
    public String addLaptopPage(Model model) {
        List<Windows> windowsKeys = windowsService.findAll();
        List<Office> officeKeys = officeService.findAll();
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("laptop", new Laptop());
        model.addAttribute("owners", employees);
        model.addAttribute("officeKeys", officeKeys);
        model.addAttribute("Keys", windowsKeys);
        return "/addLaptop";
    }

    @PostMapping({"/addLaptop"})
    public String saveLaptop(@ModelAttribute("laptop") Laptop laptop) {

        laptopService.save(laptop);
        return "/index";
    }

    @GetMapping({"/addComputer"})
    public String addComputerPage(Model model) {
        List<Windows> windowsKeys = windowsService.findAll();
        List<Office> officeKeys = officeService.findAll();
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("computer", new Computer());
        model.addAttribute("owners", employees);
        model.addAttribute("officeKeys", officeKeys);
        model.addAttribute("Keys", windowsKeys);
        return "/addComputer";
    }

    @PostMapping({"/addComputer"})
    public String saveComputer(@ModelAttribute("computer") Computer computer) {

        computerService.save(computer);
        return "/index";
    }


}
