package com.selt.controler;

import com.itextpdf.text.DocumentException;
import com.selt.model.MobilePhone;
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
public class MobilePhoneController {


    private final EmployeeService employeeService;
    private final MobilePhoneService mobilePhoneService;
    private final PhoneNumberService phoneNumberService;
    private final UserService userService;
    private final MobilePhoneRepo phoneRepo;
    private final MobilePhoneHistoryService mobilePhoneHistoryService;
    private final ConfigService configService;


    Temp temp = new Temp();

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
        return "redirect:/showUpdatePhoneForm?phoneId=" + mobilePhone.getId();
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
        model.addObject("temp", new Temp());
        temp.setNotice("Telefon wraz z ładowarką oraz oryginalnym opakowaniem.");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("historyList", mobilePhoneHistoryService.findAllByIMEI(mobilePhoneService.findById(id).get().getIMEI()));
        model.addObject("phone", mobilePhoneService.findById(id).get());
        model.addObject("temp", temp);

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
        model.addObject("employeesList", employeeService.findByOrderByLastnameAsc());
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("phoneNumberList", phoneNumberService.findByOrderByNumberAsc());
        model.addObject("phone", phoneRepo.findById(phoneId).get());
        return model;
    }

    @PostMapping({"/actionPhone/{id}"})
    public String actionPhone(@PathVariable(value = "id") long id, @ModelAttribute("temp") Temp temp) throws DocumentException, IOException {

        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);
        temp.setTempBoolen(false);
        String message = null;
        try {
            if (mobilePhone.get().getHasUser().equals(true)) {
                mobilePhoneService.getPhone(mobilePhone, temp);

            } else {
                mobilePhoneService.releasePhone(mobilePhone, temp);

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return "redirect:/showPhoneInfoForm?id=" + id;
    }


    @GetMapping({"/releasePhone/{id}"})
    public String releasePhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);
        String message = null;
        temp.setTempString("PROTOKÓŁ PRZEKAZANIA");
        temp.setTempString1("Odbierający");
        temp.setTempString2("Przekazujący");
        temp.setTempString3("Zgodnie z polityką firmy, obowiązuje całkowity zakaz podłączania kont zewnętrznych o czym zostałem poinformowany.");
        temp.setNotice("Telefon wraz z ładowarką oraz oryginalnym opakowaniem.");
        temp.setDate(String.valueOf(LocalDate.now()));
        try {


            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);
                mobilePhoneHistoryService.save(mobilePhone.get(), "WYDANIE", pdfName, LocalDate.now());
                mobilePhone.get().setHasUser(true);
                mobilePhoneService.save(mobilePhone.get());
                message = "Wydałeś telefon !";

            }

        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";


        }

        return "redirect:/showPhoneInfoForm?id=" + id;
    }

    @GetMapping({"/getPhone/{id}"})
    public String getPhone(@PathVariable(value = "id") long id) throws DocumentException, IOException {

        String message = null;
        temp.setTempString("PROTOKÓŁ ZDANIA");
        temp.setTempString1("Przekazujący");
        temp.setTempString2("Odbierający");
        temp.setTempString3("");
        Optional<MobilePhone> mobilePhone = mobilePhoneService.findById(id);
        temp.setNotice("Telefon wraz z ładowarką oraz oryginalnym opakowaniem.");
        temp.setDate(String.valueOf(LocalDate.now()));
        try {


            if (mobilePhone.get().getEmployee() != null && mobilePhone.get().getPhoneNumber() != null) {
                String pdfName = mobilePhoneHistoryService.validatePdfName(LocalDate.parse(temp.getDate()));
                ByteArrayInputStream bis = ExportPDF.protocol(mobilePhone.get(), userService.findUserByUsername().getFullname(), temp, pdfName);

                mobilePhoneHistoryService.save(mobilePhone.get(), "ZDANIE", pdfName, LocalDate.now());
                mobilePhone.get().setHasUser(false);
                mobilePhone.get().setEmployee(null);
                mobilePhone.get().setPhoneNumber(null);
                savePhone(mobilePhone.get());

            }


        } catch (StackOverflowError e) {
            message = "Nie udało się, wszystkie nazwy są już zajetę!";
        }

        return "redirect:/showPhoneInfoForm?id=" + id;
    }


    @GetMapping({"/deletePhone/{id}"})
    public String deletePhone(@PathVariable(value = "id") long id) {
        mobilePhoneService.deleteMobilePhone(id);
        getAllPhones("udało sie!");
        return "redirect:/list-phones";
    }
}
