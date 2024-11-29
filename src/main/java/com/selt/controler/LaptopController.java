package com.selt.controler;

import com.itextpdf.text.DocumentException;
import com.selt.model.Laptop;
import com.selt.model.LaptopHistory;
import com.selt.model.MobilePhoneHistory;
import com.selt.model.Temp;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LaptopController {


    private final LaptopService laptopService;
    private final EmployeeService employeeService;
    private final OfficeService officeService;
    private final UserService userService;
    private final MobilePhoneHistoryService mobilePhoneHistoryService;
    private final LaptopHistoryService laptopHistoryservice;

    Temp temp = new Temp();
    @GetMapping({"/addLaptopForm"})
    public ModelAndView addLaptopForm() {
        ModelAndView model = new ModelAndView("add-laptop-form");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("officeKeyList", officeService.findAll());
        model.addObject("laptop", new Laptop());
        model.addObject("employeesList", employeeService.findByOrderByLastnameAsc());
        return model;
    }

    @GetMapping({"/list-laptops"})
    public ModelAndView getAllLaptops() {
        ModelAndView model = new ModelAndView("list-laptops");
        model.addObject("temp", new Temp());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("laptopList", laptopService.findAll());
        return model;
    }

    @GetMapping({"/showUpdateLaptopForm"})
    public ModelAndView showUpdateLaptopForm(@RequestParam Long laptopId) {
        ModelAndView model = new ModelAndView("add-laptop-form");
        model.addObject("employeesList", employeeService.findByOrderByLastnameAsc());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("laptop", laptopService.getLaptopRepo().findById(laptopId).get());
        model.addObject("officeKeys", officeService.findAll());
        return model;
    }


    @PostMapping({"/saveLaptop"})
    public String saveLaptop(@ModelAttribute("laptop") Laptop laptop) {

//        Office office = laptop.getOfficeKey();
//        System.out.println(office);
//        if (office.getHasBeenUse().equals(false)) {
//            office.setHasBeenUse(true);
//            officeService.save(office);
//        }
//        System.out.println(office);
        laptopService.save(laptop);
        return "redirect:/showUpdateLaptopForm?laptopId=" + laptop.getId();
    }

    @PostMapping({"/list-laptops"})
    public void searchLaptops(@ModelAttribute("temp") Temp temp, Model model) {
        String mattern = '%' + temp.getTempString() + '%';
        model.addAttribute("laptopList", laptopService.search(mattern));
        model.addAttribute("username", userService.findUserByUsername().getFullname());
      //  getAllPhones("Znaleziono wyniki!");
    }

    @GetMapping({"/deleteLaptop/{id}"})
    public String deleteLaptop(@PathVariable(value = "id") long id) {
        laptopService.delete(id);
        getAllLaptops();
        return "redirect:/list-laptops";
    }


    @GetMapping({"/showLaptopInfoForm"})
    public ModelAndView showLaptopInfoForm(@RequestParam long id, String allert) {
        ModelAndView model = new ModelAndView("info-laptop-form");
        model.addObject("temp", new Temp());
        temp.setNotice("Laptop wraz z ładowarką.");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("laptop", laptopService.findById(id).get());
        model.addObject("allert", allert);
        model.addObject("laptopHistoryList", laptopHistoryservice.findAllBySerialNumber(laptopService.findById(id).get().getSerialNumber()));
        model.addObject("temp", temp);
        return model;
    }



    @GetMapping(value = "/openLaptopPDF/{id}")
    public ResponseEntity<InputStreamResource> openLaptopPDF(@PathVariable(value = "id") long id) throws FileNotFoundException {


        Optional<MobilePhoneHistory> mobilePhoneHistory = mobilePhoneHistoryService.findById(id);
        Optional<LaptopHistory> laptopHistory = laptopHistoryservice.findById(id);
        String filePath ="src/main/resources/Protocol/Laptop/";
        String fileName = laptopHistory.get().getProtocolName() + ".pdf";
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
    public String exception2(final Throwable throwable, final Model model) {
        //logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("error", errorMessage);
        return "error";
    }



    @PostMapping({"/actionLaptop/{id}"})
    public String actionLaptop(@PathVariable(value = "id") long id, @ModelAttribute("temp") Temp temp) throws DocumentException, IOException {

        Optional<Laptop> laptop = laptopService.findById(id);

        temp.setTempBoolen(false);
        String message = null;
        try {
            if (laptop.get().getHasUser().equals(true)) {
                laptopService.getLaptop(laptop, temp);

            } else {
                laptopService.releaseLaptop(laptop, temp);

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return "redirect:/showLaptopInfoForm?id=" + id;
    }


    @GetMapping({"/releaseLaptop/{id}"})
    public String releaseLaptop(@PathVariable(value = "id") long id) throws DocumentException, IOException {

        Optional<Laptop> laptop = laptopService.findById(id);
        String message = null;
        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        //temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");
        temp.setTempString3("");
        temp.setNotice("Laptop wraz z ładowarką, torba oraz myszką bezprzewodową.");
        temp.setDate(String.valueOf(LocalDate.now()));
        try {


            if (laptop.get().getEmployee() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.laptopProtocol(laptop.get(), userService.findUserByUsername().getFullname(), temp, pdfName);
                laptopHistoryservice.save(laptop.get(), "WYDANIE", pdfName, LocalDate.now());
                laptop.get().setHasUser(true);
                laptopService.save(laptop.get());
                message = "Wydałeś telefon !";

            }

        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";


        }

        return "redirect:/showLaptopInfoForm?id=" + id;
    }

    @GetMapping({"/getLaptop/{id}"})
    public String getLaptop(@PathVariable(value = "id") long id) throws DocumentException, IOException {
        Optional<Laptop> laptop = laptopService.findById(id);
        String message = null;
        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");
        temp.setNotice("Laptop wraz z ładowarką.");
        temp.setDate(String.valueOf(LocalDate.now()));
        try {


            if (laptop.get().getEmployee() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.laptopProtocol(laptop.get(), userService.findUserByUsername().getFullname(), temp, pdfName);

                laptopHistoryservice.save(laptop.get(), "ZDANIE", pdfName, LocalDate.now());
                laptop.get().setHasUser(false);
                laptop.get().setEmployee(null);
                saveLaptop(laptop.get());
            }


        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";
        }

        return "redirect:/showLaptopInfoForm?id=" + id;
    }

}
