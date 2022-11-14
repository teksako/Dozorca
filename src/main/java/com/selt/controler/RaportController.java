package com.selt.controler;

import com.selt.model.Magazine;
import com.selt.model.Raport;
import com.selt.model.Temp;
import com.selt.model.Toner;
import com.selt.repository.RaportRepo;
import com.selt.service.RaportService;
import com.selt.service.TempService;
import com.selt.service.TonerService;
import com.selt.service.UserService;
import lombok.Data;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.selt.service.ExportPDF;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@Data
public class RaportController {

    private final TonerService tonerService;
    private final RaportService raportService;
    private final RaportRepo raportRepo;
    private final TempService tempService;
    private final UserService userService;

    private List<Raport> raportList;
    @GetMapping({"/Raport"})
    public String getRaport(Model model, String alert) {
        model.addAttribute("temp", new Temp());
        model.addAttribute("id", userService.findUserByUsername().getId());
        model.addAttribute("alert", alert);

        return "/Raport";
    }

    @PostMapping({"/Raport"})
    public String getDate(@ModelAttribute("temp") Temp temp, Model model) {

        List<Raport> raport = null;
        String mattern = '%' + temp.getTempString() + '%';
        String alert="Znaleziono wyniki!";

        try{

        if (temp.getRadio() == 0) {
            raport = raportService.findAllByPreviousMonth();
        }
        if (temp.getRadio() == 2) {
            raport = raportService.findAll();
        }
        if (temp.getRadio() == 1) {
            raport = raportService.findAllByActualMonth();
        }

        if (temp.getRadio() == 3) {
            raport = raportService.findAllByDateBetween(LocalDate.parse(temp.getStart()), LocalDate.parse(temp.getEnd()));
        }
        if (temp.getRadio() == 4) {
            raport = raportService.search(mattern);
        }
        if (temp.getRadio() == 5) {
            raport = raportService.search(mattern);
        }
        if (temp.getRadio() == 6) {
            raport = raportService.findAllByTonerIsLike(mattern);
        }
        if (temp.getRadio() == 7) {
            raport = raportService.findAllByDepartmentIsLike(mattern);
        }
        }
        catch (NullPointerException exception){
            raport = raportService.findAll();
            alert="Nie znaleziono pasujących wyników, zostały wyświetlone wszystkie dane!";
        }

        model.addAttribute("raport", raport);
        model.addAttribute("alert", alert);
        getRaport(model,alert);
        raportList=raport;
        return "/Raport";

    }

    @ResponseBody
    @GetMapping({"/showRaport"})
    public List<Raport> getRaport() {
        return raportService.findAll();
    }

    @GetMapping(value = "/exportpdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> tonerReports(HttpServletResponse response) throws IOException {



        ByteArrayInputStream bis = ExportPDF.tonerRaport(raportList);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment;filename=raport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
