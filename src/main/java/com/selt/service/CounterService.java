package com.selt.service;

import com.selt.model.Counter;
import com.selt.model.OID;
import com.selt.model.Printer;
import com.selt.repository.CounterRepo;
import com.selt.repository.OIDRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

    @Scheduled(fixedDelay = 100000)
    public void validateTonerLevel(){
        List<Printer> printerList = onlineList();
        for (Printer printer: printerList) {
            int value = printerService.randomNumber();
            if (value < 15) {
                mailService.sendSimpleEmail("teksako@op.pl",
                        "Niski poziom toneru",
                        "Poziom toneru " + value + " w drukarce" + printer.getManufacturer() + " w "+ printer.getDepartment().getNameOfDepartment());
            }
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

    public Long getPrintCounter(String ip, String community, String oidval) {

        return SNMP4J.snmpGet(ip, community, oidval);

    }

    //@Scheduled(cron = "30 * * ? * ?")
    @Scheduled(cron = "0 48 15 * * MON-SUN")
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
                counter.setCounter(getPrintCounter(printer.getIPAdress(), "public", oidRepo.findOIDByoidProducentAndOidName((printer.getManufacturer()), "Total Counter").getOidValue()));
                System.out.println("Wykonano zadanie dla " + printer.getManufacturer() + " " + printer.getDepartment().getNameOfDepartment());
                //System.out.println(printer.getModel() + " " + printer.getIPAdress() +" public "+ oidRepo.findOidByoidProducentAndOidName(printer.getManufacturer(),"Total Counter").getOidValue());
                counterRepo.save(counter);
            }

        }

    }

    //----------------HP,Xerox-------------------------

    public String getTonerLevel(String ip, String oid1, String oid2) {

        Long value1 = (getPrintCounter(ip, "public", oid1));
        Long value2 = (getPrintCounter(ip, "public", oid2));// getPrintCounter(ip, "public", oid2)*100);
        double value3 = ((double) value1 / (double) value2) * 100l;
        System.out.println(value1 + " " + value2 + " " + value3);
        return String.valueOf((long) value3) + "%";
        //return String.valueOf(printerService.randomNumber());
    }

        //return String.valueOf(printerService.randomNumber());

    //-------------------

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
                for (OID oid : oidList) {
                    oidName = oidRepo.findOIDById(oid.getId()).getOidName();
                    countList.add(SNMP4J.snmpGet(ip, community, oid.getOidValue(), oidName));

                }
            } else {
                for (OID oid : oidList) {
                    if (oid.getOidName().equals("Total Counter")) {
                        oidName = oidRepo.findOIDById(oid.getId()).getOidName();
                        countList.add(SNMP4J.snmpGet(ip, community, oid.getOidValue(), oidName));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Black Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Black Max Level").get(0).getOidValue();
                        countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), oid1));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Cyan Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Cyan Max Level").get(0).getOidValue();
                        countList.add("Poziom niebieskiego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Magenta Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Magenta Max Level").get(0).getOidValue();
                        countList.add("Poziom czerwonego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1));
                        System.out.println(countList);

                    } else if ((oid.getOidName().equals("Yellow Actual Level"))) {
                        String oid1 = oidRepo.findAllByOidName("Yellow Max Level").get(0).getOidValue();
                        countList.add("Poziom żółtego toneru: " + getTonerLevel(printer.get().getIPAdress(), oid.getOidValue(), oid1));
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
        for (Counter counter: counterRepo.findAllByPrinter_idIsLike(id)
        ) {
            day =counter.getDate().getDayOfWeek().toString();
            if(day.equals("MONDAY")){
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
