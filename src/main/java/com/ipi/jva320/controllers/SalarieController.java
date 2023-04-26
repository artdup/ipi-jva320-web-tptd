package com.ipi.jva320.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;

@Controller
// @RequestMapping("/salaries")
public class SalarieController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @PostMapping("/salaries/save")
    public String saveSalarie(@ModelAttribute("salarie") SalarieAideADomicile salarie)
            throws EntityExistsException, SalarieException {
        salarieAideADomicileService.creerSalarieAideADomicile(salarie);
        return "redirect:/salaries";
    }

    @PostMapping("/salaries/{id}/update")
    public String updateSalarie(@PathVariable("id") Long id, @ModelAttribute("salarie") SalarieAideADomicile salarie)
            throws EntityNotFoundException, SalarieException {
        SalarieAideADomicile salarieToUpdate = salarieAideADomicileService.getSalarie(id);
        salarieToUpdate.setNom(salarie.getNom());
        salarieToUpdate.setMoisEnCours(salarie.getMoisEnCours());
        salarieToUpdate.setJoursTravaillesAnneeNMoins1(salarie.getJoursTravaillesAnneeNMoins1());
        salarieToUpdate.setMoisDebutContrat(salarie.getMoisDebutContrat());
        salarieToUpdate.setCongesPayesAcquisAnneeNMoins1(salarie.getCongesPayesAcquisAnneeNMoins1());
        salarieToUpdate.setCongesPayesPrisAnneeNMoins1(salarie.getCongesPayesPrisAnneeNMoins1());
        salarieAideADomicileService.updateSalarieAideADomicile(salarieToUpdate);
        salarieAideADomicileService.updateSalarieAideADomicile(salarie);
        return "redirect:/salaries/detail/" + id;
    }

    @GetMapping(value = "/salaries/detail/{id}")
    public String salarieDetail(@PathVariable("id") Long id, ModelMap model) {
        SalarieAideADomicile salarie = salarieAideADomicileService.getSalarie(id);
        if (salarie == null) {
            String errorMessage = "Le salarié n'existe pas";
            model.addAttribute("errorMessage", errorMessage);
            return "error";
            // return "redirect:/";
        }
        model.put("salarie", salarie);
        model.put("id", id);
        return "detail_Salarie";
    }

    @GetMapping("/salaries")
    public String salarieList(ModelMap model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "nom") String sortProperty) {
        List<SalarieAideADomicile> salaries = salarieAideADomicileService.getSalaries();
        model.addAttribute("salaries", salaries);
        return "list";
    }

    @GetMapping("/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable("id") Long id) {
        SalarieAideADomicileService.deleteById(id);
        return "redirect:/salaries";
    }

}
