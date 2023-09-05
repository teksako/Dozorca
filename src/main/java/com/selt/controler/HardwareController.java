package com.selt.controler;

import com.itextpdf.text.DocumentException;
import com.selt.model.*;
import com.selt.repository.MobilePhoneRepo;
import com.selt.repository.OIDRepo;
import com.selt.repository.PrinterRepo;
import com.selt.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final TempService tempService;
    private final ConfigService configService;
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
    public ModelAndView getAllPhones(String message) {
        ModelAndView model = new ModelAndView("list-phones");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("phonesList", mobilePhoneService.findAll());
        model.addObject("message", message);

        return model;
    }

    @PostMapping({"/savePhone"})
    public String savePhone(@ModelAttribute("phone") MobilePhone mobilePhone) {
        mobilePhoneService.save(mobilePhone);


        return "redirect:/list-phones";
    }

    @GetMapping(value = "/openPDF/{id}")
    public ResponseEntity<InputStreamResource> getTermsConditions(@PathVariable(value = "id") long id) throws FileNotFoundException {


        Optional<MobilePhoneHistory> mobilePhoneHistory = mobilePhoneHistoryService.findById(id);
        String filePath = configService.findById().get().getFolderPath();//"src/main/resources/Protocol/";
        String fileName = mobilePhoneHistory.get().getProtocolName() + ".pdf";
        File file = new File(filePath + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" + fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        //logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("error", errorMessage);
        return "error";
    }

    @GetMapping({"/showPhoneInfoForm"})
    public ModelAndView showPhoneInfoForm(@RequestParam long id) {
        ModelAndView model = new ModelAndView("info-mobilePhone-form");
        Temp temp = new Temp();
        temp.setNotice("Telefon wraz z ładowarką oraz oryginalnym opakowaniem.");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("historyList", mobilePhoneHistoryService.findAllByIMEI(mobilePhoneService.findById(id).get().getIMEI()));
        model.addObject("phone", mobilePhoneService.findById(id).get());
        model.addObject("temp",temp);

        return model;
    }

    @PostMapping({"/list-phones"})
    public void searchPhones(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("phonesList", mobilePhoneService.search(mattern));
        model.addAttribute("username", userService.findUserByUsername().getFullname());
        getAllPhones("Znaleziono wyniki!");
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

    @PostMapping({"/actionPhone/{id}"})
    public String actionPhone(@PathVariable(value = "id") long id, @ModelAttribute("temp") Temp temp) throws DocumentException, IOException {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println(temp.getDate());

        System.out.println(dtf1.format(LocalDate.parse(temp.getDate())));
        System.out.println(temp.getNotice());
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);
        if(mobilePhone.get().getHasUser().equals(true)){
           mobilePhoneService.releasePhone(mobilePhone,temp);
        }else{
           mobilePhoneService.getPhone(mobilePhone,temp);
       }

        return "redirect:/showPhoneInfoForm?id="+id;
    }


    @GetMapping({"/releasePhone/{id}"})
    public String releasePhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);
        String message = null;
        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");
        try {


            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.now());
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);
                mobilePhoneHistoryService.save(mobilePhone.get(), "WYDANIE", pdfName, LocalDate.now());
                mobilePhone.get().setHasUser(true);
                mobilePhoneService.save(mobilePhone.get());
                message = "Wydałeś telefon !";

            }

        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";
            //getAllPhones(message);

        }

        return "redirect:/showPhoneInfoForm?id="+id;

    }

    @GetMapping({"/getPhone/{id}"})
    public String getPhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {

        String message = null;
        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);

        try {
            //String pdfName = mobilePhoneHistoryService.validatePdfName();

            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.now());
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);

                mobilePhoneHistoryService.save(mobilePhone.get(), "ZDANIE", pdfName,LocalDate.now());
                mobilePhone.get().setHasUser(false);
                mobilePhone.get().setEmployee(null);
                mobilePhone.get().setPhoneNumber(null);
                savePhone(mobilePhone.get());
                //savePdf(mobilePhone.get());
            }


        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";
        }

        //String pdfName = LocalDate.now() + "-" + tempService.randomNumber();


        //getAllPhones("udało sie!");
        return "redirect:/showPhoneInfoForm?id="+id;
    }

    @GetMapping({"/deletePhone/{id}"})
    public String deletePhone(@PathVariable(value = "id") long id) {
        mobilePhoneService.deleteMobilePhone(id);
        getAllPhones("udało sie!");
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

    //-------------------COMPUTER---------------------------------------------
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
        return "redirect:/list-phones";
    }


    @PostMapping({"/addComputer"})
    public String saveComputer(@ModelAttribute("computer") Computer computer) {

        computerService.save(computer);
        return "/index";
    }
//-------------------END COMPUTER------------------------------------

}