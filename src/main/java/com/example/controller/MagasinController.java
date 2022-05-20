/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Categorie;
import com.example.entities.Magasin;
import com.example.repositories.MagasinRepository;
import com.example.requests.MagasinRequest;
import com.example.responses.CategorieResponse;
import com.example.responses.MagasinResponse;
import com.example.services.CategorieService;
import com.example.services.MagasinService;
import java.io.IOException;
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
@RequestMapping("/magasins")
public class MagasinController {
    
    private static final String CATEGORIES = "categories";

    private static final String MAGASIN_CREATE = "magasin/create";

    @Autowired
    private CategorieService cs;
    
    @Autowired
    private MagasinService ms;
    
    @Autowired
    private MagasinRepository mr;
    
    private final static String MAGASIN = "magasin";
    private final static String MAGASINS = "magasins";
    
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
        return "magasin/listC";
    }
    
    @GetMapping("/{cat}")
    public String listByCategories(@PathVariable("cat") Long categorie, Model model) {
        Categorie c = cs.one(categorie).orElse(null);
        if (c == null) {
            return "redirect:/magason/index";
        } else {
            model.addAttribute("categorie", categorie);
            model.addAttribute(MAGASINS, ms.all(c, true).stream().map(MagasinResponse::new).collect(Collectors.toList()));
            return "magasin/list";
        }
    }
    
    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(MAGASIN) MagasinRequest request, @PathVariable("id") Long id, Model model) {
        Magasin m = ms.one(id).orElse(null);
        if (m == null) {
            model.addAttribute("mag", request);
            model.addAttribute(MAGASIN, m);
            return "magasin/show";
        } else {
            request.setAddress(m.getAddress());
            request.setId(m.getId());
            request.setName(m.getName());
            request.setPhone(m.getPhone());
            request.setCategorie(m.getCategorie().getId());
            model.addAttribute("mag", request);
            model.addAttribute(MAGASIN, m);
            return "magasin/show";
        }
    }
    
    @GetMapping("/{categorie}/create")
    public String create(@ModelAttribute(MAGASIN) MagasinRequest request, @PathVariable("categorie") Long categorie, Model model) {
        request.setCategorie(categorie);
        model.addAttribute(MAGASIN, request);
        model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
        return MAGASIN_CREATE;
    }
    
    @PostMapping("/{categorie}/store")
    public String store(@Valid @ModelAttribute(MAGASIN) MagasinRequest request, BindingResult result, @PathVariable("categorie") Long categorie, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            return String.format("redirect:/magasins/%d/create", categorie);
        }
        Magasin m = mr.findByPhone(request.getPhone()).orElse(null);
        if (m != null) {
            result.rejectValue("phone", "doublon");
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            return MAGASIN_CREATE;
        }
        Categorie c = cs.one(request.getCategorie()).orElse(null);
        if (c == null) {
            result.rejectValue("categorie", "notFound(" + request.getCategorie() + ")");
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            return MAGASIN_CREATE;
        }
        m = new Magasin();
        m.setAddress(request.getAddress());
        m.setName(request.getName());
        m.setPhone(request.getPhone());
        m.setCreatedAt(LocalDateTime.now());
        m.setCategorie(c);
        m = ms.addOrUpdate(m);
        return String.format("redirect:/magasins/%d", m.getCategorie().getId());
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute(MAGASIN) MagasinRequest request, Model model) {
        Magasin m = ms.one(id).orElse(null);
        if (m == null) {
            model.addAttribute("mag", null);
            model.addAttribute(MAGASIN, null);
            return "magasin/edit";
        } else {
            request.setId(m.getId());
            request.setAddress(m.getAddress());
            request.setName(m.getName());
            request.setPhone(m.getPhone());
            request.setCategorie(m.getCategorie().getId());
            model.addAttribute(MAGASIN, request);
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            return "magasin/edit";
        }
    }
    
    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(MAGASIN) MagasinRequest request, BindingResult result, @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            return String.format("redirect:/magasins/%d/edit", id);
        }
        Magasin m = ms.one(id).orElse(null);
        if (m == null) {
            model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
            result.rejectValue("id", "notFound(" + id + ")");
            return String.format("redirect:/magason/%d/edit", id);
        }
        m.setAddress(request.getAddress());
        m.setName(request.getName());
        m.setPhone(request.getPhone());
        m = ms.addOrUpdate(m);
        return String.format("redirect:/magasins/%s", m.getCategorie().getId());
    }
    
    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(MAGASIN) MagasinRequest request, @PathVariable("id") Long id, Model model) throws IOException {
        Magasin magasin = mr.findById(id).orElse(null);
        if (magasin == null) {
            model.addAttribute("mag", null);
            model.addAttribute(MAGASIN, null);
            return String.format("redirect:/magasins/%d/show", id);
        } else {
            ms.delete(id);
            return String.format("redirect:/magasins/%d", id);
        }
    }
}
