/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Benevole;
import com.example.entities.Categorie;
import com.example.entities.Equipe;
import com.example.entities.Horaire;
import com.example.requests.EquipeRequest;
import com.example.responses.BenevoleResponse;
import com.example.responses.CategorieResponse;
import com.example.responses.EquipeResponse;
import com.example.responses.HoraireResponse;
import com.example.services.BenevoleService;
import com.example.services.CategorieService;
import com.example.services.EquipeService;
import com.example.services.HoraireService;
import com.example.util.CustomUserDetail;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
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
@RequestMapping("/equipes")
public class EquipeController {
    
    @Autowired
    private BenevoleService bs;

    @Autowired
    private CategorieService cs;

    @Autowired
    private EquipeService es;

    @Autowired
    private HoraireService hs;

    private static final String EQUIPE = "equipe";
    private static final String EQUIPES = "equipes";
    private static final String EQUIPE_CREATE = "equipe/create";
    private static final String REDIRECT_EQUIPES_INDEX = "redirect:/equipes/index";
    private static final String REDIRECT_EQUIPES_D_SHOW = "redirect:/equipes/%d/show";
    private static final String MEMBERS = "members";
    private static final String CATEGORIES = "categories";
    private static final String HORAIRES = "horaires";

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal CustomUserDetail principal, Model model) {
        model.addAttribute(EQUIPES,
                es.all(true).stream().map(EquipeResponse::new).collect(Collectors.toList()));
        return "equipe/list";
    }

    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(EQUIPE) EquipeRequest request, @PathVariable("id") Long id, Model model) {
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            Horaire h = hs.findByStartDateAndEndDate(e.getStart(), e.getEnd());
            request.setBen(e.getBenevole().getId());
            request.setName(e.getName());
            request.setCategorie(e.getCategorie().getId());
            request.setHoraire(h.getId());
            model.addAttribute("equip", request);
            model.addAttribute(EQUIPE, e);
            return "equipe/show";
        } else {
            return REDIRECT_EQUIPES_INDEX;
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute(EQUIPE) EquipeRequest request, Model model) {
        model.addAttribute(EQUIPE, request);
        model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
        model.addAttribute(HORAIRES, hs.all(true).stream().map(HoraireResponse::new).collect(Collectors.toList()));
        return EQUIPE_CREATE;
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(EQUIPE) EquipeRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("com.example.controller.EquipeController.store() " + result.getAllErrors());
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            model.addAttribute(HORAIRES, hs.all(true).stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return EQUIPE_CREATE;
        }
        Optional<Benevole> b = Optional.empty();
        if (request.getBen() != null) {
            b = bs.one(request.getBen());
        }
        Categorie c = cs.one(request.getCategorie()).orElse(null);
        if (c == null) {
            result.rejectValue("categorie", "notFound(" + request.getCategorie() + ")");
            return EQUIPE_CREATE;
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.rejectValue("horaire", "notFound(" + request.getHoraire() + ")");
            return EQUIPE_CREATE;
        }
        Equipe e = new Equipe();
        e.setName(request.getName());
        e.setBenevole(b.isEmpty() ? null : b.get());
        e.setCreatedAt(LocalDateTime.now());
        e.setCategorie(c);
        e.setStart(h.getStartDate());
        e.setEnd(h.getEndDate());
        e.setType(h.getType());
        Equipe e1 = es.addOrUpdate(e);
        e1.addMember(e.getBenevole());
        e1.addMembers(request.getMembers());
        es.addOrUpdate(e1);
        return REDIRECT_EQUIPES_INDEX;
    }

    @GetMapping("/{id}/edit")
    public String edit(@ModelAttribute(EQUIPE) EquipeRequest request, @PathVariable("id") Long id, Model model) {
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            Horaire h = hs.findByStartDateAndEndDate(e.getStart(), e.getEnd());
            request.setId(e.getId());
            request.setBen(e.getBenevole() != null ? e.getBenevole().getId() : null);
            request.setName(e.getName());
            request.setCategorie(e.getCategorie().getId());
            request.setHoraire(h.getId());
            request.setMembers(e.getMembers());
            model.addAttribute(EQUIPE, request);
            model.addAttribute(MEMBERS, e.getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            model.addAttribute(HORAIRES, hs.all(true).stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return "equipe/edit";
        } else {
            return REDIRECT_EQUIPES_INDEX;
        }

    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(EQUIPE) EquipeRequest request, BindingResult result, @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            return String.format("redirect:/equipes/%d/edit", id);
        }
        Categorie c = cs.one(request.getCategorie()).orElse(null);
        if (c == null) {
            result.rejectValue("categorie", "notFound(" + request.getCategorie() + ")");
            return String.format("redirect:/equipes/%d/edit", id);
        }
        Horaire h = hs.one(request.getHoraire()).orElse(null);
        if (h == null) {
            result.rejectValue("horaire", "notFound(" + request.getHoraire() + ")");
            return String.format("redirect:/equipes/%d/edit", id);
        }
        Optional<Benevole> b = bs.one(request.getBen());
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            e.setBenevole(b.isEmpty() ? null : b.get());
            e.setCategorie(c);
            e.setStart(h.getStartDate());
            e.setEnd(h.getEndDate());
            e.setName(request.getName());
            e.setType(h.getType());
            e.getMembers().clear();
            e = es.addOrUpdate(e);
            e.addMember(e.getBenevole());
            e.addMembers(request.getMembers());
            es.addOrUpdate(e);
            return REDIRECT_EQUIPES_INDEX;
        } else {
            return REDIRECT_EQUIPES_INDEX;
        }
    }

    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(EQUIPE) EquipeRequest request, @PathVariable("id") Long id, Model model) throws IOException {
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            es.delete(id);
            return REDIRECT_EQUIPES_INDEX;
        } else {
            return String.format(REDIRECT_EQUIPES_D_SHOW, id);
        }
    }

    @GetMapping("/{id}/members")
    public String members(@PathVariable("id") Long id, Model model) {
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            model.addAttribute(EQUIPE, e);
            model.addAttribute(MEMBERS, e.getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            return "equipe/members/list";
        } else {
            return String.format(REDIRECT_EQUIPES_D_SHOW, id);
        }
    }

    @GetMapping("/{idE}/{idB}/deleteMember")
    public String membersDestroy(@PathVariable("idE") Long id, @PathVariable("idB") Long idB, Model model) {
        Equipe e = es.one(id).orElse(null);
        Benevole b = bs.one(idB).orElse(null);
        if (e != null && b != null) {
            e.removeMember(b);
            e = es.addOrUpdate(e);
            model.addAttribute(EQUIPE, e);
            model.addAttribute(MEMBERS, e.getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            return "equipe/members/list";
        } else {
            return String.format(REDIRECT_EQUIPES_D_SHOW, id);
        }
    }

    @GetMapping("/{id}/membersCreate")
    public String membersCreate(@PathVariable("id") Long id, Model model) {
        Equipe e = es.one(id).orElse(null);
        if (e != null) {
            model.addAttribute(EQUIPE, e);
            model.addAttribute("userId", -1);
            model.addAttribute(MEMBERS, e.getMembers().stream().map(BenevoleResponse::new).collect(Collectors.toList()));
            return "equipe/members/create";
        } else {
            return String.format(REDIRECT_EQUIPES_D_SHOW, id);
        }
    }

}
