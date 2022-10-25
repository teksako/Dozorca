package com.selt.controler;

import com.selt.model.Counter;
import com.selt.model.Temp;
import com.selt.repository.PrinterRepo;
import com.selt.service.CounterService;
import com.selt.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Data
public class CounterController {

    //private final HardwareController hardwareController;
    private final CounterService counterService;
    private final UserService userService;
    private final PrinterRepo printerRepo;

    @GetMapping({"/list-counters"})
    //public ModelAndView  getCounters(ArrayList <Counter> counterList, String allert) {
        public ModelAndView  getCounters(@RequestParam long printerId,ArrayList <Counter> counterList, String allert) {
        ModelAndView model = new ModelAndView("list-counters");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("temp", new Temp());
        model.addObject("allert", allert);
        model.addObject("counterList", counterList);
        model.addObject("printerId", printerId);
        return model;
    }

    @PostMapping({"/subCounter"})
    public ModelAndView subCounter(@ModelAttribute("temp") Temp temp){
        ModelAndView model = new ModelAndView("list-counters");
        Long printerId = temp.getId_2();
        List<Counter> counterList = null;
        Long sub;
        LocalDate date1=LocalDate.parse(temp.getStart());
        LocalDate date2= LocalDate.parse(temp.getEnd());
        sub = counterService.subCounter(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,date1),counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,date2));
        //System.out.println("różnica = " + sub);
        String allert="Ilość wydruków: " + sub;
        counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,date1);
        //counterList.add(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,date1));
        //counterList.add(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,date2));
        return getCounters(printerId,(ArrayList<Counter>) counterList,allert);
    }

    @PostMapping({"/sortCounter"})
    public ModelAndView sortCounter(@ModelAttribute("temp") Temp temp) {

        ModelAndView model = new ModelAndView("list-counters");
        Long printerId = temp.getId_2();
        List<Counter> counterList = null;
        String allert = null;
        Long sub = null;
        //LocalDate date1=LocalDate.parse(temp.getStart());
        // LocalDate date2= LocalDate.parse(temp.getEnd());
        try{

            if (temp.getRadio() == 0) {
                counterList = counterService.findAllByPreviousMonth(printerId);

            }
            if (temp.getRadio() == 1) {
                counterList = counterService.findAllByActualMonth(printerId);

            }
            if (temp.getRadio() == 2) {
                counterList = counterService.findAllByPrinterId(printerId);

            }
            if (temp.getRadio() == 3) {
                counterList = counterService.findAllByDateBetween(printerId,LocalDate.parse(temp.getStart()), LocalDate.parse(temp.getEnd()));
               // sub = counterService.subCounter(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,LocalDate.parse(temp.getStart())),counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,LocalDate.parse(temp.getEnd())));

            }
            if (temp.getRadio() == 4) {
                counterList = counterService.findAllWeekly(printerId);
                // sub = counterService.subCounter(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,LocalDate.parse(temp.getStart())),counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId,LocalDate.parse(temp.getEnd())));

            }


            sub = counterList.get(counterList.size()-1).getCounter()-counterList.get(0).getCounter();
            //allert="Ilość wydruków: " + sub;
        }
        catch (NullPointerException exception){
            counterList = counterService.findAllByPrinterId(printerId);
            sub = counterList.get(counterList.size()-1).getCounter()-counterList.get(0).getCounter();
            if(counterList.size()==0){
                allert="Brak wyników spełniających kryteria!";
            }
        }
        catch (IndexOutOfBoundsException exception){
            allert="Brak wyników spełniających kryteria!";
        }
        allert="W wybranym okresie zostało wydrukowanych "+sub.toString()+" stron.";
        System.out.println("różnica = " + sub);


        return getCounters(printerId,(ArrayList<Counter>) counterList,allert);


    }
}
