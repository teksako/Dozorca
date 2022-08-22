package com.selt.controler;

import com.selt.service.MailService;
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

    @GetMapping("/sendMail")
    public String sendMail() throws MessagingException {
        mailService.sendSimpleEmail("pawel.kwapisinski@selt.com",
                "Wygrałeś",
                "<b>1000 000 zł</b><br>:P");
        return "wysłano";

    }
}
