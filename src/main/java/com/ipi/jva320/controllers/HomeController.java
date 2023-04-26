package com.ipi.jva320.controllers;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;

import java.time.LocalDate;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping(value = "/")
    public String home(final ModelMap model) throws Exception {
        model.put("salarieCount", salarieAideADomicileService.countSalaries()); // salarieAideADomicileService.countSalaries());

        if (salarieAideADomicileService.countSalaries() == 0) {
            SalarieAideADomicile salarie = new SalarieAideADomicile();
            salarie.setNom("Doe");
            salarie.setPrenom("John");
            salarie.setDateEmbauche(LocalDate.now());
            salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        }
        return "home";
    }

    @GetMapping(value = "/salaries/{id}")
    public String salarie(ModelMap model, @PathVariable Long id) {
        model.put("salarie", salarieAideADomicileService.getSalarie(id));
        return "detail_Salarie";
    }

    @GetMapping(value = "/salaries/new")
    public String nouveauSalarie(final ModelMap model) {
        model.put("salarie", new SalarieAideADomicile());
        return "new_Salarie";
    }

    @PostMapping(value = "/salaries")
    public String creerSalarie(@ModelAttribute("salarie") SalarieAideADomicile salarie, BindingResult result)
            throws EntityExistsException, SalarieException {
        if (result.hasErrors()) {
            return "new_Salarie";
        }
        salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        return "redirect:/salaries/" + salarie.getId();
    }

    @ControllerAdvice
    public class ExceptionHandlerController {

        @ExceptionHandler(value = { Exception.class })
        public String handleException(Exception e, Model model) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }

    }

}
