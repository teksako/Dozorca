package com.selt.config;

import com.selt.model.*;
import com.selt.repository.OIDRepo;
import com.selt.repository.RoleRepo;
import com.selt.repository.UserRepo;
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
     //oidRepo.deleteAll();
        if(oidRepo.findAll().size()==0){
            OID oid = new OID();
            oid.setOidName("Black Toner Level");
            oid.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.4");
            oid.setOidProducent("Konica Minolta");
            oidRepo.save(oid);

            OID oid1 = new OID();
            oid1.setOidName("Cyan Toner Level");
            oid1.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.3");
            oid1.setOidProducent("Konica Minolta");
            oidRepo.save(oid1);

            OID oid2 = new OID();
            oid2.setOidName("Magenta Toner Level");
            oid2.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid2.setOidProducent("Konica Minolta");
            oidRepo.save(oid2);

            OID oid3 = new OID();
            oid3.setOidName("Yellow Toner Level");
            oid3.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid3.setOidProducent("Konica Minolta");
            oidRepo.save(oid3);

            OID oid4 = new OID();
            oid4.setOidName("Total Counter");
            oid4.setOidValue(".1.3.6.1.2.1.43.10.2.1.4.1.1");
            oid4.setOidProducent("Konica Minolta");
            oidRepo.save(oid4);

            OID oid5 = new OID();
            oid5.setOidName("Total Counter");
            oid5.setOidValue(".1.3.6.1.2.1.1.16.1.1.1");
            oid5.setOidProducent("HP");
            oidRepo.save(oid5);

            OID oid6 = new OID();
            oid6.setOidName("Black Actual Level");
            oid6.setOidValue("");
            oid6.setOidProducent("Xerox");
            oidRepo.save(oid6);

            OID oid7 = new OID();
            oid7.setOidName("Black Max Level");
            oid7.setOidValue("");
            oid7.setOidProducent("Xerox");
            oidRepo.save(oid7);

            OID oid8 = new OID();
            oid8.setOidName("Cyan Actual Level");
            oid8.setOidValue("");
            oid8.setOidProducent("Xerox");
            oidRepo.save(oid8);

            OID oid9 = new OID();
            oid9.setOidName("Cyan Max Level");
            oid9.setOidValue("");
            oid9.setOidProducent("Xerox");
            oidRepo.save(oid9);

            OID oid10 = new OID();
            oid10.setOidName("Magenta Actual Level");
            oid10.setOidValue("");
            oid10.setOidProducent("Xerox");
            oidRepo.save(oid10);

            OID oid11 = new OID();
            oid11.setOidName("Magenta Max Level");
            oid11.setOidValue("");
            oid11.setOidProducent("Xerox");
            oidRepo.save(oid11);

            OID oid12 = new OID();
            oid12.setOidName("Yellow Max Level");
            oid12.setOidValue("");
            oid12.setOidProducent("Xerox");
            oidRepo.save(oid12);

            OID oid13 = new OID();
            oid13.setOidName("Yellow Actual Level");
            oid13.setOidValue("");
            oid13.setOidProducent("Xerox");
            oidRepo.save(oid13);



        }


    }

}
