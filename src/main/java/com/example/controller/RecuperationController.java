/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Equipe;
import com.example.entities.Horaire;
import com.example.entities.Recuperation;
import com.example.requests.RecuperationRequest;
import com.example.responses.BenevoleResponse;
import com.example.responses.EquipeResponse;
import com.example.responses.HoraireResponse;
import com.example.responses.RecuperationResponse;
import com.example.services.EquipeService;
import com.example.services.HoraireService;
import com.example.services.NotificationService;
import com.example.services.RecuperationService;
import com.example.util.CustomUserDetail;
import com.example.util.Statut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/recuperations")
public class RecuperationController {

    @Autowired
    private RecuperationService rs;

    @Autowired
    private EquipeService es;

    @Autowired
    private HoraireService hs;

    @Autowired
    private NotificationService ns;

    private static final String RECUP = "recuperation";
    private static final String RECUPERATIONS = "recuperations";
    private static final String RECUPERATION_CREATE = "recuperation/create";
    private static final String REDIRECT_RECUPERATIONS_INDEX = "redirect:/recuperations/index";

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(RECUPERATIONS,
                rs.all(Boolean.TRUE).stream().map(RecuperationResponse::new).collect(Collectors.toList()));
        return "recuperation/list";
    }

    @GetMapping("/{id}/show")
    public String show(@PathVariable("id") Long id, Model model,
            @ModelAttribute(RECUP) RecuperationRequest request) {
        Recuperation r = rs.one(id).orElse(null);
        if (r == null) {
            model.addAttribute(RECUP, null);
            model.addAttribute("recup", null);
            return "recuperation/show";
        } else {
            Horaire h = hs.findByStartDateAndEndDate(r.getStartDate(), r.getEndDate());
            request.setHoraire(h.getId());
            request.setEquipe(r.getEquipe().getId());
            request.setMagasins(r.getMagasins());
            request.setStatus(r.getStatus());
            model.addAttribute(RECUP, new RecuperationResponse(r));
            model.addAttribute("recup", request);
            return "recuperation/show";
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute(RECUP) RecuperationRequest request, Model model) {
        model.addAttribute(RECUP, request);
        model.addAttribute("horaires",
                hs.all("Recuperation").stream().map(HoraireResponse::new).collect(Collectors.toList()));
        return RECUPERATION_CREATE;
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(RECUP) RecuperationRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return RECUPERATION_CREATE;
        }
        Equipe e = es.one(request.getEquipe()).orElse(null);
        if (e == null) {
            result.rejectValue("equipe", "notFound(" + request.getEquipe() + ")");
            return RECUPERATION_CREATE;
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.rejectValue("horaire", "notFound(" + request.getHoraire() + ")");
            return RECUPERATION_CREATE;
        }
        Recuperation r = new Recuperation();
        r.setStartDate(h.getStartDate());
        r.setEndDate(h.getEndDate());
        r.setEquipe(e);
        Recuperation rec = rs.addOrUpdate(r);
        request.getMagasins().forEach(rec::addMagasin);
        rs.addOrUpdate(rec);
        return REDIRECT_RECUPERATIONS_INDEX;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute(RECUP) RecuperationRequest request, Model model) {
        Recuperation r = rs.one(id).orElse(null);
        if (r == null) {
            return REDIRECT_RECUPERATIONS_INDEX;
        } else {
            Horaire h = hs.findByStartDateAndEndDate(r.getStartDate(), r.getEndDate());
            request.setId(r.getId());
            request.setHoraire(h.getId());
            request.setEquipe(r.getEquipe().getId());
            request.setMagasins(r.getMagasins());
            request.setStatus(r.getStatus());
            model.addAttribute(RECUP, request);
            model.addAttribute("equipes",
                    es.all("Recuperation").stream().map(EquipeResponse::new).collect(Collectors.toList()));
            model.addAttribute("horaires",
                    hs.all("Recuperation").stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return "recuperation/edit";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(RECUP) RecuperationRequest request, BindingResult result,
            @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            return String.format("redirect:/recuperations/%d/edit", id);
        }
        Equipe e = es.one(request.getEquipe()).orElse(null);
        if (e == null) {
            result.rejectValue("equipe", "notFound(" + request.getEquipe() + ")");
            return String.format("redirect:/recuperations/%d/edit", id);
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.rejectValue("horaire", "notFound(" + request.getHoraire() + ")");
            return String.format("redirect:/recuperations/%d/edit", id);
        }
        Recuperation r = rs.one(id).orElse(null);
        if (r != null) {
            r.setStartDate(h.getStartDate());
            r.setEndDate(h.getEndDate());
            r.setEquipe(e);
            r.removeMagasins();
            Recuperation rec = rs.addOrUpdate(r);
            request.getMagasins().forEach(rec::addMagasin);
            rs.addOrUpdate(rec);
            return REDIRECT_RECUPERATIONS_INDEX;
        } else {
            return String.format("redirect:/recuperations/%d/edit", id);
        }
    }

    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(RECUP) RecuperationRequest request, @PathVariable("id") Long id, Model model,
            BindingResult result) {
        Recuperation r = rs.one(id).orElse(null);
        if (r == null) {
            return String.format("redirect:/recuperations/%d/show", id);
        } else {
            rs.delete(id);
            return REDIRECT_RECUPERATIONS_INDEX;
        }
    }

    @GetMapping("/indexChef")
    public String chefHome(@AuthenticationPrincipal CustomUserDetail principal, Model model) {
        List<Horaire> horaires = hs.all(true);
        List<Recuperation> available = new ArrayList<>();
        horaires.forEach(horaire -> {
            List<Recuperation> recs = rs.allByDates(horaire.getStartDate(), horaire.getEndDate());
            recs.forEach(r -> {
                if (r.getEquipe().getMembers().contains(principal.getUser().getBenevole())) {
                    available.add(r);
                }
            });
        });
        model.addAttribute(RECUPERATIONS,
                available.stream().map(RecuperationResponse::new).collect(Collectors.toList()));
        return "recuperation/chef";
    }

    @GetMapping("/{id}/start")
    public String startDist(@PathVariable("id") Long id) {
        Recuperation r = rs.one(id).orElse(null);
        r.setStatus(Statut.EN_COURS.name());
        r.setStart(LocalDateTime.now());
        rs.addOrUpdate(r);
        ns.notifyAdmins(
                String.format("L'équipe %s vient de demarrer sa scéance de récupération.", r.getEquipe().getName()));
        return "redirect:/recuperations/indexChef";
    }

    @GetMapping("/{id}/rapport")
    public String showRapport(@PathVariable("id") Long id, @ModelAttribute(RECUP) RecuperationRequest request,
            Model model) {
        Recuperation r = rs.one(id).orElse(null);
        request.setId(r.getId());
        request.setMagasins(r.getMagasins());
        request.setRapport(r.getRapport());
        model.addAttribute(RECUP, request);
        return "recuperation/rapport";
    }

    @PostMapping("/{id}/rapport")
    public String saveRapport(@ModelAttribute(RECUP) RecuperationRequest request, @PathVariable("id") Long id) {
        Recuperation r = rs.one(id).orElse(null);
        r.setRapport(request.getRapport());
        r.setStatus(Statut.TERMINE.name());
        r.setEnd(LocalDateTime.now());
        rs.addOrUpdate(r);
        ns.notifyAdmins(
                String.format("L'équipe %s vient de terminer sa scéance de récupération.", r.getEquipe().getName()));
        return "redirect:/recuperations/indexChef";
    }

    @GetMapping("/{id}/members")
    public String members(@PathVariable("id") Long id, Model model) {
        Recuperation r = rs.one(id).orElse(null);
        if (r != null) {
            model.addAttribute(RECUP, r);
            model.addAttribute("members",
                    r.getEquipe().getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            return "recuperation/members/list";
        } else {
            return "redirect:/home";
        }
    }
}
