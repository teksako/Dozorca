package com.selt.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;


@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

//    public void sendMail(String to,
//                         String subject,
//                         String text,
//                         boolean isHtmlContent) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//        mimeMessageHelper.setTo(to);
//        mimeMessageHelper.setSubject(subject);
//        mimeMessageHelper.setText(text, isHtmlContent);
//        javaMailSender.send(mimeMessage);
//        System.out.println("Wysłano");
//    }

    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("Dozorca Selt <pk@infokiosk.com.pl>");

        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);
        System.out.println("Wysłano");
    }
}
