package com.selt.controler;

import com.selt.service.CounterService;
import com.selt.service.MailService;
import com.selt.service.PrinterService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@RestController
public class MailApi {
    private final MailService mailService;
    private final CounterService counterService;
    private final PrinterService printerService;

    @GetMapping("/sendMail")
    public String sendMail() throws MessagingException {
//        mailService.sendSimpleEmail("pawel.kwapisinski@selt.com",
//                "Wygrałeś",
//                "<b>1000 000 zł</b><br>:P");
        counterService.validateTonerLevel();
        printerService.reload();
        return "wysłano";

    }
}
