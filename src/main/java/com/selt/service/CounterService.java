package com.selt.service;

import com.selt.model.Counter;
import com.selt.model.Printer;
import com.selt.model.Raport;
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

@Data
@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class CounterService {

    private final CounterRepo counterRepo;
    private final PrinterService printerService;
    private final OIDRepo oidRepo;

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

       return SNMP4J.snmpGetCount(ip, community, oidval);

    }

    //@Scheduled(cron = "30 * * * ? ?")
    @Scheduled(cron = "0 00 12 * * MON-SUN")
    public void save() {
        //String oidValue;
        List<Printer> printerList = printerService.findAll();
        for (Printer printer : printerList) {
            Counter counter = new Counter();
            counter.setDate(LocalDate.now());

            if(printer.getCollectCounter().equals(true)){

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
       return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id,start,end);

    }

    public List<Counter> findAllByPreviousMonth(long id) {
        LocalDate start;
        LocalDate end = null;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue()-1;
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
        return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id,start,end);

    }

    public List<Counter> findAllByDateBetween(long id,LocalDate start, LocalDate end) {
        return counterRepo.findAllByPrinter_idIsLikeAndDateIsBetween(id,start,end);

    }

    public List<Counter> findAllByPrinterId(long id) {

        return counterRepo.findAllByPrinter_idIsLike(id);
    }


}
