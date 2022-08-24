package com.selt.service;

import com.selt.model.Counter;
import com.selt.model.OID;
import com.selt.model.Printer;
import com.selt.repository.CounterRepo;
import com.selt.repository.OIDRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ietf.jgss.Oid;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Data
@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class CounterService {

    private final CounterRepo counterRepo;
    private final PrinterService printerService;
    private final OIDRepo oidRepo;
    private final MailService mailService;

    //@Scheduled(fixedDelay = 10000)
    @Scheduled(cron = "0 0 10 * * MON-SUN")
    public void validateTonerLevel() {
        List<String> mailList=new ArrayList<>();
        for (Printer printer : onlineList()) {
            long value= printerService.randomNumber();
            if(printer.getManufacturer().equals("Konica Minolta")){
                for (OID oid:printer.getOid()) {
                    if(oid.getOidName().contains("Toner Level")){
                        if (getTonerLevel(printer.getIPAdress(),oid.getOidValue()) < 10) {
                            mailList.add("Poziom toneru " + getTonerLevel(printer.getIPAdress(),oid.getOidValue()) + "% w drukarce " + printer.getManufacturer() + " w " + printer.getDepartment().getNameOfDepartment()+ "\n");
                        }
                    }
                }
            }
            else if(printer.getManufacturer().equals("Xerox")||printer.getManufacturer().equals("HP")){
                for (OID oid:printer.getOid()) {
                    if(oid.getOidName().contains("Toner Level")){
                        if (getTonerLevel(printer.getIPAdress(),oid.getOidValue()) < 10) {
                            mailList.add("Poziom toneru " + getTonerLevel(printer.getIPAdress(),oid.getOidValue()) + "% w drukarce " + printer.getManufacturer() + " w " + printer.getDepartment().getNameOfDepartment()+ "\n");
                        }
                    }
                }

            }

       }
        if(mailList.size()!=0) {
            mailService.sendSimpleEmail("pawel.kwapisinski@selt.com",
                    "Niski poziom toneru",
                    String.valueOf(mailList));
            System.out.println("Wysłano wiadomość email!");
       }
    }

    public List<Printer> onlineList() {
        List<Printer> findAll = printerService.findAll();
        List<Printer> onlineList = new ArrayList<>();
        for (Printer printer : findAll) {
            if (printer.getIPAdress() != null || printer.getIPAdress() != "-") {
                onlineList.add(printer);
            }
        }
        return onlineList;
    }
//    @Scheduled(fixedDelay = 100)
//    public void getCounter(){
//        List<Printer> printerList =printerService.findAllByOnlineIs();
//        List<OID> oidList= new ArrayList<>();
//        String oidValue = new String();
//        for (Printer printer:printerList) {
//            oidList = printer.getOid();
//            for (OID oid: oidList) {
//                if(oid.getOidName()=="Total Counter"){
//                    oidValue=oid.getOidValue();
//                }
//
//            }
//            System.out.println(printer.getManufacturer()+ " " + printer.getModel() +" "+printerService.getPrintCounter(printer.getIPAdress(),oidValue));
//
//        }
//
//    }



    //@Scheduled(cron = "30 * * ? * ?")
    @Scheduled(cron = "0 30 8 * * MON-SUN")
    public void save() {
        //String oidValue;
        List<Printer> printerList = printerService.findAll();
        for (Printer printer : printerList) {
            Counter counter = new Counter();
            counter.setDate(LocalDate.now());

            if (printer.getCollectCounter().equals(true)) {

                //oidValue = oidRepo.findOIDByoidProducentAndOidName((printer.getManufacturer()),"Total Counter").getOidValue();
                counter.setPrinter(printer);
                //counter.setCounter((long) printerService.randomNumber());
                try {
                    counter.setCounter(getPrintCounter(printer.getIPAdress(), "public", oidRepo.findOIDByoidProducentAndOidName((printer.getManufacturer()), "Total Counter").getOidValue()));
                    System.out.println(LocalDateTime.now() + " " + "Wykonano zadanie dla " + printer.getManufacturer() + " " + printer.getDepartment().getNameOfDepartment());
                    //System.out.println(printer.getModel() + " " + printer.getIPAdress() +" public "+ oidRepo.findOidByoidProducentAndOidName(printer.getManufacturer(),"Total Counter").getOidValue());
                    counterRepo.save(counter);
                }
                catch (NullPointerException e){
                    System.out.println(LocalDateTime.now() + " " + "Nie wykonano zadania dla " + printer.getManufacturer() + " "+ printer.getIPAdress() + " "  + printer.getDepartment().getNameOfDepartment());
                }
                catch (NumberFormatException e){
                    System.out.println(LocalDateTime.now() + " " + "Nie wykonano zadania dla " + printer.getManufacturer() + " "+ printer.getIPAdress() + " " + printer.getDepartment().getNameOfDepartment());
                }
            }

        }

    }

    public Long getPrintCounter(String ip, String community, String oidval) {

        return SNMP4J.snmpGet(ip, community, oidval);

    }

    //----------------HP,Xerox-------------------------

    public Long getTonerLevel(String ip, String oid1, String oid2) {

        Long value1 = (getPrintCounter(ip, "public", oid1));
        Long value2 = (getPrintCounter(ip, "public", oid2));// getPrintCounter(ip, "public", oid2)*100);
        double value3 = ((double) value1 / (double) value2) * 100l;
        System.out.println(value1 + " " + value2 + " " + value3);
        return (long)value3;

    }

    //-------------------Konica Minolta-----------------
    public Long getTonerLevel(String ip, String oid){
        return getPrintCounter(ip, "public", oid);
    }




    public List<String> getActualCounter(long id) {

        Optional<Printer> printer = printerService.findById(id);
        List<OID> oidList = printer.get().getOid();
        List<String> countList = new ArrayList<>();
        String community = "public";
        String oidName;
        String ip = printer.get().getIPAdress();
        if (printer.get().getIPAdress().equals("-")) {
            countList.add("Drukarka nie podłączona do sieci!");
        } else {
            if (printer.get().getManufacturer().equals("Konica Minolta")) {
                if (printer.get().getModel().contains("454") || printer.get().getModel().contains("284") || printer.get().getModel().contains("360") || printer.get().getModel().contains("253")) {
                    for (OID oid : oidList) {
                        if (oid.getOidName().equals("Total Counter")) {
                            countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        } else if (oid.getOidName().equals("Black Toner Level")) {
                            countList.add("Poziom czarnego toneru: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()) + "%");
                        } else if (oid.getOidName().equals("Cyan Toner Level")) {
                            countList.add("Poziom niebieskiego toneru: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()) + "%");
                        } else if (oid.getOidName().equals("Magenta Toner Level")) {
                            countList.add("Poziom czerwonego toneru: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()) + "%");
                        } else if (oid.getOidName().equals("Yellow Toner Level")) {
                            countList.add("Poziom żółtego toneru: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()) + "%");
                            ;
                        }

                    }
                } else if (printer.get().getModel().contains("3300") || printer.get().getModel().contains("4700")) {
                    for (OID oid : oidList) {
                        if (oid.getOidName().equals("Total Counter")) {
                            countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        } else if ((oid.getOidName().equals("Actual Toner Capacity"))) {
                            String oid1 = oidRepo.findAllByOidName("Max Toner Capacity").get(0).getOidValue();
                            countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), oid1) + "%");
                        } else if ((oid.getOidName().equals("Actual Drum Condition"))) {
                            String oid1 = oidRepo.findAllByOidName("Max Drum Condition").get(0).getOidValue();
                            countList.add("Kondycja bębna: " + getTonerLevel(ip, oid.getOidValue(), oid1) + "%");
                        }
                    }
                } else {
                    for (OID oid : oidList) {
                        if (oid.getOidName().equals("Total Counter")) {
                            countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        }
                    }
                }

            } else if (printer.get().getModel().contains("3335")) {
                for (OID oid : oidList) {
                    if (oid.getOidName().equals("Total Counter")) {
                        oidName = oidRepo.findOIDById(oid.getId()).getOidName();
                        countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Black Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Black Max Level").get(0).getOidValue();
                        countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Actual Drum Condition"))) {
                        String oid1 = oidRepo.findAllByOidName("Max Drum Condition").get(0).getOidValue();
                        countList.add("Kondycja bębna: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);
                    }
                }
            } else if (printer.get().getModel().contains("B235")) {
                for (OID oid : oidList) {
                    if (oid.getOidName().equals("Total Counter")) {
                        oidName = oidRepo.findOIDById(oid.getId()).getOidName();
                        countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Black Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Black Max Level").get(0).getOidValue();
                        countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Actual Drum Condition B235"))) {
                        String oid1 = oidRepo.findAllByOidName("Max Drum Condition B235").get(0).getOidValue();
                        countList.add("Kondycja bębna: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);
                    }
                }
            } else {
                for (OID oid : oidList) {
                    if (oid.getOidName().equals("Total Counter")) {
                        oidName = oidRepo.findOIDById(oid.getId()).getOidName();
                        countList.add("ILość wydrukowanych stron: " + SNMP4J.snmpGet(ip, community, oid.getOidValue()));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Black Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Black Max Level").get(0).getOidValue();
                        countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Cyan Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Cyan Max Level").get(0).getOidValue();
                        countList.add("Poziom niebieskiego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Magenta Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Magenta Max Level").get(0).getOidValue();
                        countList.add("Poziom czerwonego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        ;
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Yellow Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Yellow Max Level").get(0).getOidValue();
                        countList.add("Poziom żółtego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);
                    } else if ((oid.getOidName().equals("Actual Drum Condition"))) {
                        String oid1 = oidRepo.findAllByOidName("Max Drum Condition").get(0).getOidValue();
                        countList.add("Kondycja bębna: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1) + "%");
                        System.out.println(countList);
                    }
                }
            }
        }

        return countList;
    }

    public List<Counter> findAllByActualMonth(long id) {
        LocalDate start;
        LocalDate end;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        start = LocalDate.of(year, month, 1);
        int day;

        if (year % 4 == 0) {

            if (month == 2) {
                end = LocalDate.of(year, month, 29);
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                end = LocalDate.of(year, month, 31);
            } else {
                end = LocalDate.of(year, month, 30);
            }
        } else {
            if (month == 2) {
                end = LocalDate.of(year, month, 28);
            } else {
                end = LocalDate.of(year, month, 30);
            }

        }
        return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id, start, end);

    }

    public List<Counter> findAllByPreviousMonth(long id) {
        LocalDate start;
        LocalDate end = null;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue() - 1;
        start = LocalDate.of(year, month, 1);
        int day;

        if (year % 4 == 0) {

            if (month == 2) {
                end = LocalDate.of(year, month, 29);
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                end = LocalDate.of(year, month, 31);
            } else {
                end = LocalDate.of(year, month, 30);
            }
        } else {
            if (month == 2) {
                end = LocalDate.of(year, month, 28);
            } else {
                end = LocalDate.of(year, month, 30);
            }

        }
        return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id, start, end);

    }

    public List<Counter> findAllByDateBetween(long id, LocalDate start, LocalDate end) {
        return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id, start, end);

    }

    public List<Counter> findAllByPrinterId(long id) {
        return counterRepo.findAllByPrinter_idIsLike(id);
    }

    public List<Counter> findAllWeekly(long id) {
        List<Counter> counterList = new ArrayList<>();
        String day;
        for (Counter counter : counterRepo.findAllByPrinter_idIsLike(id)
        ) {
            day = counter.getDate().getDayOfWeek().toString();
            if (day.equals("MONDAY")) {
                counterList.add(counter);
                System.out.println(counter);
            }
        }
        return counterList;
    }

    public Counter findByPrinter_IdIsLikeAndDateIsLike(long id, LocalDate date) {
        return counterRepo.findByPrinter_IdIsLikeAndDateIsLike(id, date);
    }

    public Long subCounter(Counter counter, Counter counter2) {

        return counter2.getCounter() - counter.getCounter();
    }


}
