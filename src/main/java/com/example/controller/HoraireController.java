/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Horaire;
import com.example.requests.HoraireRequest;
import com.example.responses.HoraireResponse;
import com.example.services.HoraireService;
import com.example.services.NotificationService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Yves
 */
@Controller
@RequestMapping("/horaires")
public class HoraireController {

    @Autowired
    private HoraireService hs;

    @Autowired
    private NotificationService ns;

    private static final String HORAIRE = "horaire";
    private static final String HORAIRES = "horaires";
    private static final String REDIRECT_INDEX = "redirect:/horaires/index";
    private static final String HORAIRE_CREATE = "horaire/create";
    private static final String HORAIRE_EDIT = "redirect:/horaires/%d/edit";

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(HORAIRES,
                hs.all(true).stream().map(HoraireResponse::new).collect(Collectors.toList()));
        return "horaire/list";
    }

    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(HORAIRE) HoraireRequest request, @PathVariable("id") Long id, Model model) {
        Horaire h = hs.one(id).orElse(null);
        if (h != null) {
            request.setId(h.getId());
            request.setStart(h.getStartDate().toString());
            request.setEnd(h.getEndDate().toString());
            request.setType(h.getType());
            request.setColor(h.getColor());
            model.addAttribute(HORAIRE, new HoraireResponse(h));
            model.addAttribute("hor", request);
            return "horaire/show";
        } else {
            return REDIRECT_INDEX;
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute(HORAIRE) HoraireRequest request, Model model) {
        model.addAttribute(HORAIRE, request);
        return HORAIRE_CREATE;
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(HORAIRE) HoraireRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return HORAIRE_CREATE;
        }
        LocalDateTime start = LocalDateTime.parse(request.getStart());
        LocalDateTime end = LocalDateTime.parse(request.getEnd());
        if (start.isBefore(LocalDateTime.now())) {
            result.rejectValue("start", "invalidStartDate");
            return HORAIRE_CREATE;
        }
        if (start.isAfter(end)) {
            result.rejectValue("end", "invalidEndDate");
            return HORAIRE_CREATE;
        }
        Horaire h = new Horaire();
        h.setStartDate(start);
        h.setEndDate(end);
        h.setType(request.getType());
        h.setColor(request.getColor());
        h = hs.addOrUpdate(h);
        ns.notifyBenevoles(String.format("Une période de %s vient d'etre créee, veuillez marquer votre disponibilité.", h.getType()));
        return REDIRECT_INDEX;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute(HORAIRE) HoraireRequest request, Model model) {
        Horaire h = hs.one(id).orElse(null);
        if (h != null) {
            request.setStart(h.getStartDate().toString());
            request.setEnd(h.getEndDate().toString());
            request.setId(h.getId());
            request.setType(h.getType());
            request.setColor(h.getColor());
            model.addAttribute(HORAIRE, request);
            return "horaire/edit";
        } else {
            model.addAttribute(HORAIRE, null);
            return "horaire/edit";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(HORAIRE) HoraireRequest request, BindingResult result, @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            return String.format(HORAIRE_EDIT, id);
        }
        Horaire h = hs.one(id).orElse(null);
        if (h != null) {
            LocalDateTime start = LocalDateTime.parse(request.getStart());
            LocalDateTime end = LocalDateTime.parse(request.getEnd());
            if (start.isBefore(LocalDateTime.now())) {
                result.rejectValue("start", "invalidStartDate");
            }
            if (start.isAfter(end)) {
                result.rejectValue("end", "invalidEndDate");
                return String.format(HORAIRE_EDIT, id);
            }
            h.setStartDate(start);
            h.setEndDate(end);
            h.setType(request.getType());
            h.setColor(request.getColor());
            hs.addOrUpdate(h);
            return REDIRECT_INDEX;
        } else {
            result.rejectValue("id", "notFound(" + id + ")");
            return String.format(HORAIRE_EDIT, id);
        }
    }

    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(HORAIRE) HoraireRequest request, @PathVariable("id") Long id, Model model) {
        Horaire h = hs.one(id).orElse(null);
        if (h == null) {
            return String.format("redirect:/horaires/%s/show", id);
        } else {
            hs.delete(id);
            return REDIRECT_INDEX;
        }
    }
}
