/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Benevole;
import com.example.entities.Distribution;
import com.example.entities.Equipe;
import com.example.entities.Horaire;
import com.example.requests.DistributionRequest;
import com.example.responses.BenevoleResponse;
import com.example.responses.DistributionResponse;
import com.example.responses.EquipeResponse;
import com.example.responses.HoraireResponse;
import com.example.services.DistributionService;
import com.example.services.EquipeService;
import com.example.services.HoraireService;
import com.example.services.NotificationService;
import com.example.util.CustomUserDetail;
import com.example.util.Statut;
import java.io.IOException;
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
@RequestMapping("/distributions")
public class DistributionController {
    
    @Autowired
    private DistributionService ds;
    
    @Autowired
    private EquipeService es;
    
    @Autowired
    private HoraireService hs;

    @Autowired
    private NotificationService ns;
    
    private final static String DISTRIB = "distribution";
    
    private final static String DISTRIBUTIONS = "distributions";
    
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(DISTRIBUTIONS,
                ds.all(true).stream().map(DistributionResponse::new).collect(Collectors.toList()));
        return "distribution/list";
    }
    
    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(DISTRIB) DistributionRequest request, @PathVariable("id") Long id, Model model) {
        Distribution d = ds.one(id).orElse(null);
        if (d == null) {
            model.addAttribute(DISTRIB, null);
            model.addAttribute("dist", null);
            return "distribution/show";
        } else {
            request.setEquipe(d.getEquipe().getId());
            request.setLieux(d.getLocation());
            request.setStatut(d.getStatut());
            model.addAttribute(DISTRIB, new DistributionResponse(d));
            model.addAttribute("dist", request);
            return "distribution/show";
        }
    }
    
    @GetMapping("/create")
    public String create(@ModelAttribute(DISTRIB) DistributionRequest request, Model model) {
        model.addAttribute(DISTRIB, request);
        model.addAttribute("horaires", hs.all("Distribution").stream().map(HoraireResponse::new).collect(Collectors.toList()));
        return "distribution/create";
    }
    
    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(DISTRIB) DistributionRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "distribution/create";
        }
        Equipe e = es.one(request.getEquipe()).orElse(null);
        if (e == null) {
            result.rejectValue("equipe", "notFound(" + request.getEquipe() + ")");
            return "distribution/create";
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.reject("horaire", "notFound(" + request.getHoraire() + ")");
            return "distribution/create";
        }
        Distribution d = new Distribution();
        d.setLocation(request.getLieux());
        d.setEquipe(e);
        d.setStartDate(h.getStartDate());
        d.setEndDate(h.getEndDate());
        d.setStatut(Statut.EN_ATTENTE.name());
        ds.addOrUpdate(d);
        return "redirect:/distributions/index";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute(DISTRIB) DistributionRequest request, Model model) {
        Distribution d = ds.one(id).orElse(null);
        if (d == null) {
            return "redirect:/distributions/index";
        } else {
            Horaire h = hs.findByStartDateAndEndDate(d.getStartDate(), d.getEndDate());
            request.setHoraire(h.getId());
            request.setEquipe(d.getEquipe().getId());
            request.setLieux(d.getLocation());
            request.setStatut(d.getStatut());
            request.setId(d.getId());
            model.addAttribute(DISTRIB, request);
            model.addAttribute("equipes", es.all("Distribution").stream().map(EquipeResponse::new).collect(Collectors.toList()));
            model.addAttribute("horaires", hs.all("Distribution").stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return "distribution/edit";
        }
    }
    
    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(DISTRIB) DistributionRequest request, BindingResult result, @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            return String.format("redirect:/distributions/%d/edit", id);
        }
        Equipe e = es.one(request.getEquipe()).orElse(null);
        if (e == null) {
            result.rejectValue("equipe", "notFound(" + request.getEquipe() + ")");
            return String.format("redirect:/distributions/%d/edit", id);
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.reject("horaire", "notFound(" + request.getHoraire() + ")");
            return String.format("redirect:/distributions/%d/edit", id);
        }
        Distribution d = ds.one(id).orElse(null);
        if (d == null) {
            result.rejectValue("id", "notFound(" + id + ")");
            return String.format("redirect:/distributions/%d/edit", id);
        } else {
            d.setStartDate(h.getStartDate());
            d.setEndDate(h.getEndDate());
            d.setEquipe(e);
            d.setLocation(request.getLieux());
            ds.addOrUpdate(d);
            return "redirect:/distributions/index";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(DISTRIB) DistributionRequest request, BindingResult result, @PathVariable("id") Long id, Model model) throws IOException {
        Distribution d = ds.one(id).orElse(null);
        if (d == null) {
            return "redirect:/distributions/index";
        } else {
            ds.delete(id);
            return "redirect:/distributions/index";
        }
    }
    
    @GetMapping("/indexChef")
    public String chefHome(@AuthenticationPrincipal CustomUserDetail principal, Model model) {
        List<Horaire> horaires = hs.all(true);
        List<Distribution> available = new ArrayList<>();
        horaires.forEach(horaire -> {
            List<Distribution> dist = ds.allByDates(horaire.getStartDate(), horaire.getEndDate());
            dist.forEach(d -> {
                if (d.getEquipe().getMembers().contains(principal.getUser().getBenevole())) {
                    available.add(d);
                }
            });
        });
        model.addAttribute(DISTRIBUTIONS, available.stream().map(DistributionResponse::new).collect(Collectors.toList()));
        return "distribution/chef";
    }
    
    @GetMapping("/{id}/start")
    public String startDist(@PathVariable("id") Long id) {
        Distribution d = ds.one(id).orElse(null);
        d.setStatut(Statut.EN_COURS.name());
        d.setStart(LocalDateTime.now());
        ds.addOrUpdate(d);
        ns.notifyAdmins(String.format("L'équipe %s vient de demarrer sa scéance de distribution dans la localité de: %s.", d.getEquipe().getName(), d.getLocation()));
        return "redirect:/distributions/indexChef";
    }
    
    @GetMapping("/{id}/rapport")
    public String showRapport(@PathVariable("id") Long id, @ModelAttribute(DISTRIB) DistributionRequest request, Model model) {
        Distribution d = ds.one(id).orElse(null);
        request.setId(d.getId());
        request.setEquipe(d.getEquipe().getId());
        request.setLieux(d.getLocation());
        request.setRapport(d.getRapport());
        model.addAttribute(DISTRIB, request);
        return "distribution/rapport";
    }
    
    @PostMapping("/{id}/rapport")
    public String saveRapport(@ModelAttribute(DISTRIB) DistributionRequest request, @PathVariable("id") Long id) {
        Distribution d = ds.one(id).orElse(null);
        d.setRapport(request.getRapport());
        d.setStatut(Statut.TERMINE.name());
        d.setEnd(LocalDateTime.now());
        ds.addOrUpdate(d);
        ns.notifyAdmins(String.format("L'équipe %s vient de terminer sa scéance de distribution dans la localité de: %s.", d.getEquipe().getName(), d.getLocation()));
        return "redirect:/distributions/indexChef";
    }

    @GetMapping("/{id}/members")
    public String members(@PathVariable("id") Long id, Model model) {
        Distribution d = ds.one(id).orElse(null);
        if (d != null) {
            model.addAttribute(DISTRIB, d);
            model.addAttribute("members", d.getEquipe().getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            return "distribution/members/list";
        } else {
            return "redirect:/home";
        }
    }

}
