package com.selt.config;

import com.selt.model.*;
import com.selt.repository.*;
import com.selt.service.MailService;
import com.selt.service.PrinterService;
import com.selt.service.SNMP4J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
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
    @Autowired
    private final ConfigRepo configRepo;
    private final MailService mailService;
    @Autowired
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final LocationRepo locationRepo;
    private final PhoneNumberRepo numberRepo;
    private final TonerRepo tonerRepo;


    @Autowired
    public SeltApplication(ConfigRepo configRepo
            , MailService mailService
            , EmployeeRepo employeeRepo
            , DepartmentRepo departmentRepo
            , LocationRepo locationRepo
            , PhoneNumberRepo numberRepo
            , TonerRepo tonerRepo) {

        this.configRepo = configRepo;
        this.mailService = mailService;
        this.numberRepo = numberRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.locationRepo = locationRepo;
        this.tonerRepo = tonerRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(SeltApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

//        for (Employee employee:employeeRepo.findAll()) {
//            if(employee.getDepartment()==null){
//                employee.setDepartment(departmentRepo.getById(39l));
//            }
//            employeeRepo.save(employee);
//        }

        //ExportPDF.protcol();
        final List<String> printerList = new ArrayList<>();
        printerList.add("454");
        printerList.add("284");
        printerList.add("253");
        printerList.add("360");

        final List<String> printerList1 = new ArrayList<>();
        printerList1.add("bizhub 20");
        printerList1.add("bizhub 20P");

        final List<String> printerList2 = new ArrayList<>();
        printerList2.add("3300P");
        printerList2.add("4700P");

        final List<String> printerList3 = new ArrayList<>();//wszystkie
        printerList3.addAll(printerList1);
        printerList3.addAll(printerList);
        printerList3.addAll(printerList2);

        final List<String> printerList4 = new ArrayList<>();//20,20p,3300p,4700p
        printerList4.addAll(printerList1);
        printerList4.addAll(printerList2);

        final List<String> printerList5 = new ArrayList<>();//3335
        printerList5.add("3335");

        final List<String> printerList11 = new ArrayList<>();//B235
        printerList11.add("B235");

        final List<String> printerList6 = new ArrayList<>();//C235,b235,3335
        printerList6.add("C235");
        printerList6.addAll(printerList5);
        printerList6.addAll(printerList11);

        final List<String> printerList7 = new ArrayList<>();//C235
        printerList7.add("C235");

        final List<String> printerList8 = new ArrayList<>();//HP Color
        printerList8.add("MFP M277");
        printerList8.add("MFP M283");

        final List<String> printerList9 = new ArrayList<>();//HP Black
        printerList9.add("M402dne");
        printerList9.add("M1217nfw");
        printerList9.add("M26NW");
        printerList9.add("1320n");
        printerList9.add("P2055dn");
        printerList9.add("M426fdw");
        printerList9.add("M1536dnf");
        printerList9.add("P1606dn");
        printerList9.add("M227");
        printerList9.add("M203dn");
        printerList9.add("m201n");
        printerList9.add("M125nw");
        printerList9.add("137fnw");

        final List<String> printerList10 = new ArrayList<>();//HP All
        printerList10.addAll(printerList9);
        printerList10.addAll(printerList8);

        final List<String> printerList12 = new ArrayList<>();
        printerList12.add("M203dn");
        printerList12.add("M227sdn");
//
//        Location location = new Location();
//        Department department = new Department();
//        PhoneNumber number = new PhoneNumber();

//        if(numberRepo.findAll().size()==0){
//            number.setNumber("-");
//            number.setSIMNumber("-");
//            number.setPIN("-");
//            number.setPUK("-");
//            numberRepo.save(number);
//        }
//        if (locationRepo.findAll().size() == 0) {
//
//            location.setNameOfLocation("-");
//            location.setCity("-");
//            location.setNumber("-");
//            location.setCity("-");
//            locationRepo.save(location);
//        }
//
//        if (departmentRepo.findAll().size() == 0) {
//            List<Location> locationList = new ArrayList<>();
//            locationList.add(location);
//            department.setNameOfDepartment("-");
//            department.setLocations(locationList);
//            departmentRepo.save(department);
//        }
//
//        if(tonerRepo.findAll().size()==0){
//            Toner toner = new Toner();
//            toner.setTonerName("-");
//            tonerRepo.save(toner);
//
//        }
//
//
//        if (employeeRepo.findAll().size() == 0) {
//            Employee employee = new Employee();
//            employee.setDepartment(department);
//            employee.setFirstname("-");
//            employee.setWorkplace("-");
//            employee.setLastname("");
//            employeeRepo.save(employee);
//
//        }

        if (configRepo.findAll().size() == 0) {
            Configuration configuration = new Configuration();
            configuration.setEmail("admin@admin.com");
            configuration.setServiceCallcounter(10000l);
            configuration.setTime(100l);
            configuration.setTonerPercent(10l);
            configRepo.save(configuration);
        }


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

        if (oidRepo.findAll().size() == 0) {

            //-------------Konica Minolta--------------//
            OID oid = new OID();
            oid.setOidName("Black Toner Level");
            oid.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.4");
            oid.setOidProducent("Konica Minolta");
            oid.setPrinterModel(printerList);
            oidRepo.save(oid);

            OID oid1 = new OID();
            oid1.setOidName("Cyan Toner Level");
            oid1.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.1");
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
            oid3.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.3");
            oid3.setOidProducent("Konica Minolta");
            oid3.setPrinterModel(printerList);
            oidRepo.save(oid3);

            OID oid4 = new OID();
            oid4.setOidName("Total Counter");
            oid4.setOidValue("1.3.6.1.2.1.43.10.2.1.4.1.1");
            oid4.setOidProducent("Konica Minolta");
            oid4.setPrinterModel(printerList3);
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
            oid13.setOidName("Fusing Unit Condition");
            oid13.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.14");
            oid13.setOidProducent("Konica Minolta");
            oid13.setPrinterModel(printerList);
            oidRepo.save(oid13);

            OID oid14 = new OID();
            oid14.setOidName("Transfer Belt Condition");
            oid14.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.15");
            oid14.setOidProducent("Konica Minolta");
            oid14.setPrinterModel(printerList);
            oidRepo.save(oid14);

            OID oid15 = new OID();
            oid15.setOidName("TRansfer Roller Condition");
            oid15.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.16");
            oid15.setOidProducent("Konica Minolta");
            oid15.setPrinterModel(printerList);
            oidRepo.save(oid15);

            OID oid16 = new OID();
            oid16.setOidName("Ozon Filter Condition");
            oid16.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.17");
            oid16.setOidProducent("Konica Minolta");
            oid16.setPrinterModel(printerList);
            oidRepo.save(oid16);

            OID oid17 = new OID();
            oid17.setOidName("Toner Filter Condition");
            oid17.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.18");
            oid17.setOidProducent("Konica Minolta");
            oid17.setPrinterModel(printerList);
            oidRepo.save(oid17);

            OID oid18 = new OID();
            oid18.setOidName("Actual Drum Page Counter");
            oid18.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid18.setOidProducent("Konica Minolta");
            oid18.setPrinterModel(printerList4);
            oidRepo.save(oid18);

            OID oid19 = new OID();
            oid19.setOidName("Max Drum Page Counter");
            oid19.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.2");
            oid19.setOidProducent("Konica Minolta");
            oid19.setPrinterModel(printerList4);
            oidRepo.save(oid19);

            OID oid20 = new OID();
            oid20.setOidName("Max Toner Capacity");
            oid20.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.1");
            oid20.setOidProducent("Konica Minolta");
            oid20.setPrinterModel(printerList2);
            oidRepo.save(oid20);

            OID oid21 = new OID();
            oid21.setOidName("Actual Toner Capacity");
            oid21.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid21.setOidProducent("Konica Minolta");
            oid21.setPrinterModel(printerList2);
            oidRepo.save(oid21);

            //-----------------XEROX-----------------------------//

            OID oid22 = new OID();
            oid22.setOidName("Total Counter");
            oid22.setOidValue("1.3.6.1.4.1.253.8.53.13.2.1.6.1.20.34");
            oid22.setPrinterModel(printerList6);
            oid22.setOidProducent("Xerox");
            oidRepo.save(oid22);

            OID oid23 = new OID();
            oid23.setOidName("Max Drum Page Counter");
            oid23.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.2");
            oid23.setPrinterModel(printerList5);
            oid23.setOidProducent("Xerox");
            oidRepo.save(oid23);

            OID oid24 = new OID();
            oid24.setOidName("Actual Toner Capacity");
            oid24.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid24.setOidProducent("Xerox");
            oid24.setPrinterModel(printerList6);
            oidRepo.save(oid24);

            OID oid25 = new OID();
            oid25.setOidName("Max Toner Capacity");
            oid25.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.1");
            oid25.setPrinterModel(printerList6);
            oid25.setOidProducent("Xerox");
            oidRepo.save(oid25);

            OID oid26 = new OID();
            oid26.setOidName("Actual Cyan Toner Capacity");
            oid26.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid26.setPrinterModel(printerList7);
            oid26.setOidProducent("Xerox");
            oidRepo.save(oid26);

            OID oid27 = new OID();
            oid27.setOidName("Max Cyan Toner Capacity");
            oid27.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.2");
            oid27.setPrinterModel(printerList7);
            oid27.setOidProducent("Xerox");
            oidRepo.save(oid27);

            OID oid28 = new OID();
            oid28.setOidName("Actual Magenta Toner Capacity");
            oid28.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.3");
            oid28.setPrinterModel(printerList7);
            oid28.setOidProducent("Xerox");
            oidRepo.save(oid28);

            OID oid29 = new OID();
            oid29.setOidName("Max Magenta Toner Capacity");
            oid29.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.3");
            oid29.setPrinterModel(printerList7);
            oid29.setOidProducent("Xerox");
            oidRepo.save(oid29);

            OID oid30 = new OID();
            oid30.setOidName("Actual Yellow Toner Capacity");
            oid30.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.4");
            oid30.setPrinterModel(printerList7);
            oid30.setOidProducent("Xerox");
            oidRepo.save(oid30);

            OID oid31 = new OID();
            oid31.setOidName("Max Yellow Toner Capacity");
            oid31.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.4");
            oid31.setPrinterModel(printerList7);
            oid31.setOidProducent("Xerox");
            oidRepo.save(oid31);


            //----------------HP-------------------------------//

            OID oid32 = new OID();
            oid32.setOidName("Total Counter");
            oid32.setOidValue(".1.3.6.1.2.1.43.10.2.1.4.1.1");
            oid32.setPrinterModel(printerList10);
            oid32.setOidProducent("HP");
            oidRepo.save(oid32);

            OID oid33 = new OID();
            oid33.setOidName("Max Toner Capacity");
            oid33.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.1");
            oid33.setPrinterModel(printerList10);
            oid33.setOidProducent("HP");
            oidRepo.save(oid33);

            OID oid34 = new OID();
            oid34.setOidName("Actual Toner Capacity");
            oid34.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.1");
            oid34.setPrinterModel(printerList10);
            oid34.setOidProducent("HP");
            oidRepo.save(oid34);

            OID oid35 = new OID();
            oid35.setOidName("Max Cyan Toner Capacity");
            oid35.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.2");
            oid35.setPrinterModel(printerList8);
            oid35.setOidProducent("HP");
            oidRepo.save(oid35);

            OID oid36 = new OID();
            oid36.setOidName("Actual Cyan Toner Capacity");
            oid36.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.2");
            oid36.setPrinterModel(printerList8);
            oid36.setOidProducent("HP");
            oidRepo.save(oid36);

            OID oid37 = new OID();
            oid37.setOidName("Max Magenta Toner Capacity");
            oid37.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.3");
            oid37.setPrinterModel(printerList8);
            oid37.setOidProducent("HP");
            oidRepo.save(oid37);

            OID oid38 = new OID();
            oid38.setOidName("Actual Magenta Toner Capacity");
            oid38.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.3");
            oid38.setPrinterModel(printerList8);
            oid38.setOidProducent("HP");
            oidRepo.save(oid38);

            OID oid39 = new OID();
            oid39.setOidName("Max Yellow Toner Capacity");
            oid39.setOidValue(".1.3.6.1.2.1.43.11.1.1.8.1.4");
            oid39.setPrinterModel(printerList8);
            oid39.setOidProducent("HP");
            oidRepo.save(oid39);

            OID oid40 = new OID();
            oid40.setOidName("Actual Yellow Toner Capacity");
            oid40.setOidValue(".1.3.6.1.2.1.43.11.1.1.9.1.4");
            oid40.setPrinterModel(printerList8);
            oid40.setOidProducent("HP");
            oidRepo.save(oid40);


            //------------------Xerox-----------------------------------------
            OID oid41 = new OID();
            oid41.setOidName("Max Drum Page Counter");
            oid41.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.2");//3335
            oid41.setPrinterModel(printerList5);
            oid41.setOidProducent("Xerox");
            oidRepo.save(oid41);

            OID oid42 = new OID();
            oid42.setOidName("Actual Drum Page Counter");
            oid42.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.14");//235
            oid42.setPrinterModel(printerList11);
            oid42.setOidProducent("Xerox");
            oidRepo.save(oid42);

            OID oid43 = new OID();
            oid43.setOidName("Max Drum Page Counter");
            oid43.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.14");//235
            oid43.setPrinterModel(printerList11);
            oid43.setOidProducent("Xerox");
            oidRepo.save(oid43);

            OID oid44 = new OID();
            oid44.setOidName("Max Drum Page Counter");
            oid44.setOidValue("1.3.6.1.2.1.43.11.1.1.8.1.5");//203,227
            oid44.setPrinterModel(printerList12);
            oid44.setOidProducent("HP");
            oidRepo.save(oid44);

            OID oid45 = new OID();
            oid45.setOidName("Actual Drum Page Counter");
            oid45.setOidValue("1.3.6.1.2.1.43.11.1.1.9.1.5");//235
            oid45.setPrinterModel(printerList12);
            oid45.setOidProducent("HP");
            oidRepo.save(oid45);

        }


    }

}
