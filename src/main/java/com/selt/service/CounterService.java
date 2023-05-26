package com.selt.service;

import com.selt.model.Configuration;
import com.selt.model.Counter;
import com.selt.model.OID;
import com.selt.model.Printer;
import com.selt.repository.CounterRepo;
import com.selt.repository.OIDRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ietf.jgss.Oid;
import org.snmp4j.smi.Null;
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
    private final ConfigService configService;


     //@Scheduled(fixedDelay = 10000)
    @Scheduled(cron = "0 30 8 * * MON-SUN")
    public void validateTonerLevel() {
        List<String> mailList = new ArrayList<>();
        Long tonerPercent = configService.getConfigRepo().getById(1l).getTonerPercent();
        for (Printer printer : onlineList()) {
            //long value = printerService.randomNumber();
            Long tonerLevel = 0l;
            try {
                for (OID oid : printer.getOid()) {
                    if (oid.getOidName().contains("Toner Level")) {
                        tonerLevel = getTonerLevel(printer.getIPAdress(), oid.getOidValue());
                        if (tonerLevel < tonerPercent) {
                            mailList.add("Poziom toneru " + translate(oid) + " wynosi " + tonerLevel + "% w drukarce " + printer.getManufacturer() + " " + printer.getModel() + " w " + printer.getDepartment().getNameOfDepartment() + "\n");
                        }
                    } else if ((oid.getOidName().equals("Actual Toner Capacity"))) {
                        tonerLevel = getTonerLevel(printer.getIPAdress(), oid.getOidValue(), findOID(printer.getOid(), "Max Toner Capacity").getOidValue());
                        if (tonerLevel < tonerPercent) {
                            mailList.add("Poziom toneru " + translate(oid) + " wynosi " + tonerLevel + "% w drukarce " + printer.getManufacturer() + " " + printer.getModel() + " w " + printer.getDepartment().getNameOfDepartment() + "\n");
                        }
                    } else if ((oid.getOidName().equals("Actual Cyan Toner Capacity"))) {
                        tonerLevel = getTonerLevel(printer.getIPAdress(), oid.getOidValue(), findOID(printer.getOid(), "Max Cyan Toner Capacity").getOidValue());
                        if (tonerLevel < tonerPercent) {
                            mailList.add("Poziom toneru " + translate(oid) + " wynosi " + tonerLevel + "% w drukarce " + printer.getManufacturer() + " " + printer.getModel() + " w " + printer.getDepartment().getNameOfDepartment() + "\n");
                        }
                    } else if ((oid.getOidName().equals("Actual Magenta Toner Capacity"))) {
                        tonerLevel = getTonerLevel(printer.getIPAdress(), oid.getOidValue(), findOID(printer.getOid(), "Max Magenta Toner Capacity").getOidValue());
                        if (tonerLevel < tonerPercent) {
                            mailList.add("Poziom toneru " + translate(oid) + " wynosi " + tonerLevel + "% w drukarce " + printer.getManufacturer() + " " + printer.getModel() + " w " + printer.getDepartment().getNameOfDepartment() + "\n");
                        }
                    } else if ((oid.getOidName().equals("Actual Yellow Toner Capacity"))) {
                        tonerLevel = getTonerLevel(printer.getIPAdress(), oid.getOidValue(), findOID(printer.getOid(), "Max Yellow Toner Capacity").getOidValue());
                        if (tonerLevel < tonerPercent) {
                            mailList.add("Poziom toneru " + translate(oid) + " wynosi " + tonerLevel + "% w drukarce " + printer.getManufacturer() + " " + printer.getModel() + " w " + printer.getDepartment().getNameOfDepartment() + "\n");
                        }
                    }
                }
            } catch (NullPointerException exception) {
                System.out.println("nie udało sie wykonać zadania dla " + printer.getIPAdress());
            }
        }
        System.out.println(mailList);
        if (mailList.size() != 0) {
            mailService.sendSimpleEmail(configService.getConfigRepo().getById(1l).getEmail(),
                    "Dozorca - niski poziom toneru!",
                    String.valueOf(mailList));
            System.out.println("Wysłano wiadomość email!");
        }
    }

    public String translate(OID oid) {
        if (oid.getOidName().contains("Cyan")) {
            return "niebieskiego";
        } else if (oid.getOidName().contains("Magenta")) {
            return "czerwonego";
        } else if (oid.getOidName().contains("Yellow")) {
            return "żółtego";
        } else {
            return "czarnego";
        }
    }

    public List<Printer> onlineList() {
        List<Printer> findAll = printerService.findAll();
        List<Printer> onlineList = new ArrayList<>();
        for (Printer printer : findAll) {
            if (printer.getIPAdress() != "-") {
                onlineList.add(printer);
            }
        }
        System.out.println(onlineList);
        return onlineList;
    }



    //@Scheduled(cron = "30 * * ? * ?")
    @Scheduled(cron = "0 0 9 * * MON-SUN")
    public void save() {

        List<Printer> printerList = printerService.findAll();
        for (Printer printer : printerList) {
            Counter counter = new Counter();
            counter.setDate(LocalDate.now());

            if (printer.getCollectCounter().equals(true)) {
                counter.setPrinter(printer);
                try {
                    counter.setCounter(getPrintCounter(printer.getIPAdress(), "public", oidRepo.findOIDByoidProducentAndOidName((printer.getManufacturer()), "Total Counter").getOidValue()));
                    System.out.println(LocalDateTime.now() + " " + "Wykonano zadanie dla " + printer.getManufacturer() + " " + printer.getDepartment().getNameOfDepartment());
                    counterRepo.save(counter);
                } catch (NullPointerException e) {
                    System.out.println(LocalDateTime.now() + " " + "Nie wykonano zadania dla " + printer.getManufacturer() + " " + printer.getIPAdress() + " " + printer.getDepartment().getNameOfDepartment());
                } catch (NumberFormatException e) {
                    System.out.println(LocalDateTime.now() + " " + "Nie wykonano zadania dla " + printer.getManufacturer() + " " + printer.getIPAdress() + " " + printer.getDepartment().getNameOfDepartment());
                }
            }

        }

    }

    public Long getPrintCounter(String ip, String community, String oidval) {
        try {
            return SNMP4J.snmpGet(ip, community, oidval);
        } catch (NullPointerException e) {
            System.out.println(ip);
            return 0l;
        }

    }

    //----------------HP,Xerox-------------------------

    public Long getTonerLevel(String ip, String oid1, String oid2) {
        double value3;
        Long value1 = null;
        Long value2 = null;
        try {
            value1 = (getPrintCounter(ip, "public", oid1));
            value2 = (getPrintCounter(ip, "public", oid2));// getPrintCounter(ip, "public", oid2)*100);
            value3 = ((double) value1 / (double) value2) * 100l;

            System.out.println(ip + " " + value1 + " " + value2 + " " + value3);
        } catch (NullPointerException e) {
            System.out.println(ip);
            value3 = ((double) value1 / (double) value2) * 100l;

        }
        return (long) value3;


    }

    //-------------------Konica Minolta-----------------
    public Long getTonerLevel(String ip, String oid) {
        return getPrintCounter(ip, "public", oid);
    }


    public OID findOID(List<OID> oidList, String variable) {
        OID oid1 = new OID();
        for (OID oid : oidList) {
            if (oid.getOidName().contains(variable)) {
                oid1 = oid;
            }
        }
        return oid1;
    }


    public List<String> getActualCounter(long id) {

        Optional<Printer> printer = printerService.findById(id);
        List<OID> oidList = printer.get().getOid();
        List<String> countList = new ArrayList<>();
        String community = "public";
        String ip = printer.get().getIPAdress();
        if (printer.get().getIPAdress().equals("-")) {
            countList.add("Drukarka nie podłączona do sieci!");
        } else {

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
                } else if ((oid.getOidName().equals("Actual Toner Capacity"))) {
                    //String oid1 = oidRepo.findAllByOidName("Max Toner Capacity").get(0).getOidValue();
                    countList.add("Poziom czarnego toneru: " + getTonerLevel(ip, oid.getOidValue(), findOID(oidList, "Max Toner Capacity").getOidValue()) + "%");
                } else if ((oid.getOidName().equals("Actual Drum Page Counter"))) {
                    if (printer.get().getModel().equals("bizhub 20")||printer.get().getModel().equals("bizhub 20p")) {
                        countList.add("Kondycja bębna: " + (long) (((double) SNMP4J.snmpGet(ip, community, oid.getOidValue()) / 25000) * 100) + "%");
                        System.out.println(Math.ceil((long) (((double) SNMP4J.snmpGet(ip, community, oid.getOidValue()) / 25000) * 100)) + "%");
                    } else {
                        countList.add("Kondycja bębna: " + getTonerLevel(ip, oid.getOidValue(), findOID(oidList, "Max Drum Page Counter").getOidValue()) + "%");
                    }
                } else if ((oid.getOidName().equals("Actual Cyan Toner Capacity"))) {
                    //String oid1 = oidRepo.findAllByOidName("Cyan Max Level").get(0).getOidValue();
                    countList.add("Poziom niebieskiego toneru: " + getTonerLevel(ip, oid.getOidValue(), findOID(oidList, "Max Cyan Toner Capacity").getOidValue()) + "%");
                    System.out.println(countList);

                } else if ((oid.getOidName().equals("Actual Magenta Toner Capacity"))) {
                    //String oid1 = oidRepo.findAllByOidName("Magenta Max Level").get(0).getOidValue();
                    countList.add("Poziom czerwonego toneru: " + getTonerLevel(ip, oid.getOidValue(), findOID(oidList, "Max Magenta Toner Capacity").getOidValue()) + "%");

                    System.out.println(countList);

                } else if ((oid.getOidName().equals("Actual Yellow Toner Capacity"))) {
                    //String oid1 = oidRepo.findAllByOidName("Yellow Max Level").get(0).getOidValue();
                    countList.add("Poziom żółtego toneru: " + getTonerLevel(ip, oid.getOidValue(), findOID(oidList, "Max Yellow Toner Capacity").getOidValue()) + "%");
                    System.out.println(countList);
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
