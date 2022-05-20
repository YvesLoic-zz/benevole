/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Categorie;
import com.example.requests.CategorieRequest;
import com.example.responses.CategorieResponse;
import com.example.services.CategorieService;
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
@RequestMapping("/categories")
public class CategorieController {

    @Autowired
    private CategorieService cs;

    private final static String CATEGORIE = "categorie";
    private final static String CATEGORIES = "categories";

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(CATEGORIES, cs.all(true).stream().map(CategorieResponse::new).collect(Collectors.toList()));
        return "categorie/list";
    }

    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(CATEGORIE) CategorieRequest request, @PathVariable("id") Long id, Model model) {
        Categorie c = cs.one(id).orElse(null);
        if (c == null) {
            model.addAttribute("cat", request);
            model.addAttribute(CATEGORIE, c);
            return "categorie/show";
        } else {
            request.setId(c.getId());
            request.setName(c.getName());
            model.addAttribute("cat", request);
            model.addAttribute(CATEGORIE, c);
            return "categorie/show";
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute(CATEGORIE) CategorieRequest request, Model model) {
        model.addAttribute(CATEGORIE, request);
        return "categorie/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(CATEGORIE) CategorieRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categorie/create";
        }
        Categorie c = new Categorie();
        c.setName(request.getName());
        c.setCreatedAt(LocalDateTime.now());
        cs.addOrUpdate(c);
        return "redirect:/categories/index";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute(CATEGORIE) CategorieRequest request, Model model) {
        Categorie c = cs.one(id).orElse(null);
        if (c == null) {
            model.addAttribute("cat", null);
            model.addAttribute(CATEGORIE, null);
            return "categorie/edit";
        } else {
            request.setId(c.getId());
            request.setName(c.getName());
            model.addAttribute(CATEGORIE, request);
            return "categorie/edit";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute(CATEGORIE) CategorieRequest request, BindingResult result, @PathVariable("id") Long id,
            Model model) {
        if (result.hasErrors()) {
            return String.format("redirect:edit", id);
        }
        Categorie c = cs.one(id).orElse(null);
        if (c == null) {
            result.rejectValue("id", "notFound(" + id + ")");
            return String.format("redirect:/categories/%d/edit", id);
        }
        c.setName(request.getName());
        cs.addOrUpdate(c);
        return "redirect:/categories/index";
    }

    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(CATEGORIE) CategorieRequest request, @PathVariable("id") Long id, Model model) throws IOException {
        Categorie c = cs.one(id).orElse(null);
        if (c == null) {
            model.addAttribute("cat", null);
            model.addAttribute(CATEGORIE, null);
            return String.format("redirect:/categories/%d/show", id);
        } else {
            cs.delete(id);
            return "redirect:/categories/index";
        }
    }
}
