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


    private final CounterService counterService;
    private final UserService userService;
    private final PrinterRepo printerRepo;

    @GetMapping({"/list-counters"})

    public ModelAndView getCounters(@RequestParam long printerId, ArrayList<Counter> counterList, String allert) {
        ModelAndView model = new ModelAndView("list-counters");
        model.addObject("username", userService.findUserByUsername().getFullname());
        model.addObject("temp", new Temp());
        model.addObject("allert", allert);
        model.addObject("counterList", counterList);
        model.addObject("printerId", printerId);
        model.addObject("id", userService.findUserByUsername().getId());
        return model;
    }

    @PostMapping({"/subCounter"})
    public ModelAndView subCounter(@ModelAttribute("temp") Temp temp) {
        ModelAndView model = new ModelAndView("list-counters");
        Long printerId = temp.getId_2();
        List<Counter> counterList = null;
        Long sub;
        LocalDate date1 = LocalDate.parse(temp.getStart());
        LocalDate date2 = LocalDate.parse(temp.getEnd());
        sub = counterService.subCounter(counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId, date1), counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId, date2));
        String allert = "Ilość wydruków: " + sub;
        counterService.findByPrinter_IdIsLikeAndDateIsLike(printerId, date1);
        return getCounters(printerId, (ArrayList<Counter>) counterList, allert);
    }

    @PostMapping({"/sortCounter"})
    public ModelAndView sortCounter(@ModelAttribute("temp") Temp temp) {

        ModelAndView model = new ModelAndView("list-counters");
        Long printerId = temp.getId_2();
        List<Counter> counterList = null;
        String allert = null;
        Long sub = null;

        try {

            if (temp.getRadio() == 0) {
                counterList = counterService.findAllByPreviousMonth(printerId);
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";
            }
            if (temp.getRadio() == 1) {
                counterList = counterService.findAllByActualMonth(printerId);
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";
            }
            if (temp.getRadio() == 2) {
                counterList = counterService.findAllByPrinterId(printerId);
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";
            }
            if (temp.getRadio() == 3) {
                counterList = counterService.findAllByDateBetween(printerId, LocalDate.parse(temp.getStart()), LocalDate.parse(temp.getEnd()));
                sub = counterList.get(counterList.size() - 1).getCounter() - counterList.get(0).getCounter();
                allert = "W okresie od "+LocalDate.parse(temp.getStart())+" do "+LocalDate.parse(temp.getEnd())+" zostało wydrukowanych " + sub + " stron.";

            }
            if (temp.getRadio() == 4) {
                counterList = counterService.findAllWeekly(printerId);
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";

            }
            if (temp.getRadio() == 5) {
                counterList = counterService.findAllMonthly(printerId);
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";

            }


            sub = counterList.get(counterList.size() - 1).getCounter() - counterList.get(0).getCounter();

        } catch (NullPointerException exception) {
            try {
                counterList = counterService.findAllByPrinterId(printerId);
                sub = counterList.get(counterList.size() - 1).getCounter() - counterList.get(0).getCounter();
                if (counterList.size() == 0) {
                    allert = "Brak wyników spełniających kryteria!";
                }
                allert = "W wybranym okresie zostało wydrukowanych " + sub + " stron.";
            } catch (IndexOutOfBoundsException exception2) {
                allert = "Brak wyników spełniających kryteria!";
            }

        } catch (IndexOutOfBoundsException exception) {
            allert = "Brak wyników spełniających kryteria!";
        }
        sub = counterList.get(counterList.size() - 1).getCounter() - counterList.get(0).getCounter();

        System.out.println("różnica = " + sub);
        counterService.printAnalysis(printerId);

        return getCounters(printerId, (ArrayList<Counter>) counterList, allert);


    }
}
