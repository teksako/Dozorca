package com.selt.config;

import com.selt.model.*;
import com.selt.repository.OIDRepo;
import com.selt.repository.RoleRepo;
import com.selt.repository.UserRepo;
import com.selt.service.MailService;
import com.selt.service.PrinterService;
import com.selt.service.SNMP4J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.selt")
@EntityScan("com.selt.model")
@EnableJpaRepositories("com.selt.repository")
@EnableScheduling
@Import(SecurityConfig.class)
public class SeltApplication implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private OIDRepo oidRepo;
    private SNMP4J snmp4J;
    private PrinterService printerService;
    private final MailService mailService;

    @Autowired
    public SeltApplication(MailService mailService) {

        this.mailService = mailService;

    }

    public static void main(String[] args) {
        SpringApplication.run(SeltApplication.class, args);
    }

    //@Scheduled(cron = "0 13 21 ? * MON")
//
//    public void writeSomething1() {
//        //printerService.getCounter();
//       //System.out.println("test");
//        printerService.test();
//    }

    @Override
    public void run(String... args) throws Exception {

       final List<String> printerList = new ArrayList<>();
        printerList.add("454");
        printerList.add("284");
        printerList.add("253");
        printerList.add("360");
        final List<String> printerList1 = new ArrayList<>();
        printerList1.add("3300P");
        printerList1.add("4700P");
        printerList1.add("bizhub 20");
        printerList1.add("bizhub 20P");
        if (userRepository.findAll().size() == 0) {
            UserRole userRole = new UserRole();
            userRole.setRole(Role.ADMIN);
            userRole = roleRepository.save(userRole);
            UserRole userRole2 = new UserRole();
            userRole2.setRole(Role.USER);

            roleRepository.save(userRole2);


            User user = new User();
            user.setLogin("user");
            user.setPassword(encoder.encode("omg11thc"));
            user.setCreateDate(new Date());
            user.setRoles(Arrays.asList(userRole2));
            //user.setRole(userRole2);
            user.setEnabled(true);
            user.setFullname("User");
            userRepository.save(user);

            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(encoder.encode("omg11thc"));
            admin.setCreateDate(new Date());
            admin.setRoles(Arrays.asList(userRole));
            admin.setFullname("Admin");
            //admin.setRole(userRole);
            admin.setEnabled(true);
            userRepository.save(admin);
        }

        if(oidRepo.findAll().size()==0){
            OID oid = new OID();
            oid.setOidName("Black Toner Level");
            oid.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.4");
            oid.setOidProducent("Konica Minolta");
            oid.setPrinterModel(printerList);
            oidRepo.save(oid);

            OID oid1 = new OID();
            oid1.setOidName("Cyan Toner Level");
            oid1.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.3");
            oid1.setOidProducent("Konica Minolta");
            oid1.setPrinterModel(printerList);
            oidRepo.save(oid1);

            OID oid2 = new OID();
            oid2.setOidName("Magenta Toner Level");
            oid2.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid2.setOidProducent("Konica Minolta");
            oid2.setPrinterModel(printerList);
            oidRepo.save(oid2);

            OID oid3 = new OID();
            oid3.setOidName("Yellow Toner Level");
            oid3.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid3.setOidProducent("Konica Minolta");
            oid3.setPrinterModel(printerList);
            oidRepo.save(oid3);

            OID oid4 = new OID();
            oid4.setOidName("Total Counter");
            oid4.setOidValue("1.3.6.1.2.1.43.10.2.1.4.1.1");
            oid4.setOidProducent("Konica Minolta");
            oidRepo.save(oid4);

            OID oid5 = new OID();
            oid5.setOidName("Cyan Drum Condition");
            oid5.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.5");
            oid5.setOidProducent("Konica Minolta");
            oid5.setPrinterModel(printerList);
            oidRepo.save(oid5);

            OID oid6 = new OID();
            oid6.setOidName("Magenta Drum Condition");
            oid6.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.7");
            oid6.setOidProducent("Konica Minolta");
            oid6.setPrinterModel(printerList);
            oidRepo.save(oid6);

            OID oid7 = new OID();
            oid7.setOidName("Yellow Drum Condition");
            oid7.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.9");
            oid7.setOidProducent("Konica Minolta");
            oid.setPrinterModel(printerList);
            oidRepo.save(oid7);

            OID oid8 = new OID();
            oid8.setOidName("Black Drum Condition");
            oid8.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.11");
            oid8.setOidProducent("Konica Minolta");
            oid8.setPrinterModel(printerList);
            oidRepo.save(oid8);

            OID oid9 = new OID();
            oid9.setOidName("Cyan Developer Condition");
            oid9.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.6");
            oid9.setOidProducent("Konica Minolta");
            oid9.setPrinterModel(printerList);
            oidRepo.save(oid9);

            OID oid10 = new OID();
            oid10.setOidName("Magenta Developer Condition");
            oid10.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.8");
            oid10.setOidProducent("Konica Minolta");
            oid10.setPrinterModel(printerList);
            oidRepo.save(oid10);

            OID oid11 = new OID();
            oid11.setOidName("Yellow Developer Condiotion");
            oid11.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.10");
            oid11.setOidProducent("Konica Minolta");
            oid11.setPrinterModel(printerList);
            oidRepo.save(oid11);

            OID oid12 = new OID();
            oid12.setOidName("Black Developer Condition");
            oid12.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.12");
            oid12.setOidProducent("Konica Minolta");
            oid12.setPrinterModel(printerList);
            oidRepo.save(oid12);

            OID oid13 = new OID();
            oid13.setOidName("Max Toner Capacity");
            oid13.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.1");
            oid13.setOidProducent("Konica Minolta");
            oidRepo.save(oid13);

            OID oid14 = new OID();
            oid14.setOidName("Actual Toner Capacity");
            oid14.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid14.setOidProducent("Konica Minolta");
            oidRepo.save(oid14);

            OID oid15 = new OID();
            oid15.setOidName("Max Drum Condition");
            oid15.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.2");
            oid15.setOidProducent("Konica Minolta");
            oidRepo.save(oid15);

            OID oid16 = new OID();
            oid16.setOidName("Actual Drum Condition");
            oid16.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid14.setOidProducent("Konica Minolta");
            oidRepo.save(oid16);

            OID oid17 = new OID();
            oid17.setOidName("Total Counter");
            oid17.setOidValue("1.3.6.1.4.1.253.8.53.13.2.1.6.1.20.34");
            oid17.setOidProducent("Xerox");
            oidRepo.save(oid17);

            OID oid18 = new OID();
            oid18.setOidName("Drum Page Counter");
            oid18.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid18.setOidProducent("Konica Minolta");
            oidRepo.save(oid18);

        }


    }

}
