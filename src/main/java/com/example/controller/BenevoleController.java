/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Benevole;
import com.example.entities.Horaire;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.repositories.RoleRepository;
import com.example.requests.BenevoleRequest;
import com.example.responses.BenevoleResponse;
import com.example.responses.EquipeResponse;
import com.example.responses.HoraireResponse;
import com.example.responses.RoleResponse;
import com.example.services.BenevoleService;
import com.example.services.HoraireService;
import com.example.services.UserService;
import com.example.util.CustomUserDetail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yves
 */
@Controller
@RequestMapping("/benevoles")
public class BenevoleController {

    @Autowired
    private BenevoleService bs;

    @Autowired
    private UserService us;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository rr;

    @Autowired
    private HoraireService hs;

    private static final String BENEVOLE = "benevole";
    private static final String BENEVOLES = "benevoles";
    private static final String BENEVOLE_CREATE = "benevole/create";
    private static final String BENEVOLE_EDIT = "benevole/edit";
    private static final String DOUBLON = "doublon";
    private static final double DAYS = 0;
    private static final String HORAIRES = "horaires";
    private static final String ROLES = "roles";
    private static final String REDIRECT_BENEVOLES_INDEX = "redirect:/benevoles/index";
    private static final String REDIRECT_BENEVOLES_INDEX_BEN = "redirect:/benevoles/indexBen";

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute(BENEVOLES,
                bs.all(Boolean.TRUE).stream().map(BenevoleResponse::new).collect(Collectors.toList()));
        return "benevole/list";
    }

    @GetMapping("/{id}/show")
    public String show(@ModelAttribute(BENEVOLE) BenevoleRequest request, @PathVariable("id") Long id, Model model) {
        Benevole b = bs.one(id).orElse(null);
        if (b != null) {
            request.setUsername(b.getUsername().getUsername());
            request.setName(b.getName());
            request.setLastName(b.getLastName());
            request.setAddress(b.getAddress());
            request.setCode(b.getCode());
            request.setVille(b.getVille());
            request.setEmail(b.getEmail());
            request.setPhone(b.getPhone());
            request.setProfession(b.getProfession());
            request.setSexe(b.getSexe());
            request.setDateNaiss(b.getBirthDate().toString());
            request.setPermis(b.isPermis());
            request.setChef(b.isChef());
            model.addAttribute(BENEVOLE, request);
            model.addAttribute("benModel", b);
            return "benevole/show";
        } else {
            model.addAttribute(BENEVOLE, null);
            return "benevole/show";
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute(BENEVOLE) BenevoleRequest request, Model model) {
        model.addAttribute(BENEVOLE, request);
        model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
        return BENEVOLE_CREATE;
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(BENEVOLE) BenevoleRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        if (us.usernameExist(request.getUsername(), Optional.empty())) {
            result.rejectValue("username", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        if (bs.emailExist(request.getEmail(), Optional.empty())) {
            result.rejectValue("email", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        if (bs.phoneExist(request.getPhone(), Optional.empty())) {
            result.rejectValue("phone", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        LocalDate date = LocalDate.parse(request.getDateNaiss());
        if (date.isAfter(LocalDate.now())) {
            result.rejectValue("dateNaiss", "passDate");
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        User user = new User(request.getUsername(), encoder.encode(request.getPassword()));
        user.addRole(getRoles(request.getIdRole()));
        us.addOrUpdate(user);
        Benevole b = new Benevole();
        b.setUsername(user);
        b.setName(request.getName());
        b.setLastName(request.getLastName());
        b.setAddress(request.getAddress());
        b.setCode(request.getCode());
        b.setVille(request.getVille());
        b.setEmail(request.getEmail());
        b.setPhone(request.getPhone());
        b.setPassword(request.getPassword());
        b.setProfession(request.getProfession());
        b.setSexe(request.getSexe());
        b.setBirthDate(date);
        b.setPermis(request.isPermis());
        b.setChef(request.isChef());
        b.setCreated_at(LocalDateTime.now());
        bs.addOrUpdate(b);
        return REDIRECT_BENEVOLES_INDEX;
    }

    @GetMapping("/{id}/edit")
    public String edit(@ModelAttribute("benevole") BenevoleRequest request, @PathVariable("id") Long id, Model model) {
        Benevole b = bs.one(id).orElse(null);
        if (b != null) {
            request.setUsername(b.getUsername().getUsername());
            request.setName(b.getName());
            request.setLastName(b.getLastName());
            request.setAddress(b.getAddress());
            request.setCode(b.getCode());
            request.setVille(b.getVille());
            request.setEmail(b.getEmail());
            request.setPhone(b.getPhone());
            request.setPassword(b.getPassword());
            request.setProfession(b.getProfession());
            request.setSexe(b.getSexe());
            request.setDateNaiss(b.getBirthDate().toString());
            request.setPermis(b.isPermis());
            request.setChef(b.isChef());
            model.addAttribute(BENEVOLE, request);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_EDIT;
        } else {
            model.addAttribute(BENEVOLE, null);
            return BENEVOLE_EDIT;
        }
    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute("benevole") BenevoleRequest request, BindingResult result,
            @PathVariable("id") Long id, Model model) {
        if (result.hasErrors()) {
            return String.format("redirect:/benevoles/%d/edit", id);
        }
        Optional<Benevole> benevole = bs.one(id);
        Optional<User> user = us.one(request.getUsername());
        if (benevole.isEmpty()) {
            result.rejectValue("id", "notFound(" + id + ")");
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_EDIT;
        }
        if (!us.usernameExist(request.getUsername(), user)) {
            result.rejectValue("username", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_EDIT;
        }
        if (!bs.emailExist(request.getEmail(), benevole)) {
            result.rejectValue("email", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_EDIT;
        }
        if (!bs.phoneExist(request.getPhone(), benevole)) {
            result.rejectValue("phone", DOUBLON);
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_EDIT;
        }
        LocalDate date = LocalDate.parse(request.getDateNaiss());
        if (date.isAfter(LocalDate.now())) {
            result.rejectValue("dateNaiss", "passDate");
            model.addAttribute(ROLES, rr.findAll().stream().map(RoleResponse::new).collect(Collectors.toList()));
            return BENEVOLE_CREATE;
        }
        Benevole b = benevole.get();
        User u = user.isPresent() ? user.get() : null;
        u.setUsername(request.getUsername());
        u.removeRole();
        u = us.addOrUpdate(u);
        u.addRole(getRoles(request.getIdRole()));
        u = us.addOrUpdate(u);
        b.setUsername(u);
        b.setName(request.getName());
        b.setLastName(request.getLastName());
        b.setAddress(request.getAddress());
        b.setCode(request.getCode());
        b.setVille(request.getVille());
        b.setEmail(request.getEmail());
        b.setPhone(request.getPhone());
        b.setProfession(request.getProfession());
        b.setSexe(request.getSexe());
        b.setBirthDate(date);
        b.setPermis(request.isPermis());
        b.setChef(request.isChef());
        bs.addOrUpdate(b);
        return REDIRECT_BENEVOLES_INDEX;
    }

    @PostMapping("/{id}/delete")
    public String destroy(@ModelAttribute(BENEVOLE) BenevoleRequest request, BindingResult result,
            @PathVariable("id") Long id, Model model) {
        Benevole benevole = bs.one(id).orElse(null);
        if (benevole == null) {
            result.rejectValue("id", "notFound(" + id + ")");
            model.addAttribute(BENEVOLE, request);
            return String.format("redirect:/benevoles/%d/show", id);
        } else {
            bs.delete(id);
            return REDIRECT_BENEVOLES_INDEX;
        }
    }

    /**
     * Méthode de définition des roles d'un user/bénévole
     * 
     * @param value Identifiant du role à attribuer à l'utilisateur
     * 
     * @return La liste des roles à attribuer à un user/bénévole 
     */
    private List<Role> getRoles(Long value) {
        List<Role> roles = rr.findAll();
        List<Role> userRoles;
        if (value.intValue() == 1) {
            userRoles = List.of(roles.get(0));
        } else {
            userRoles = roles;
        }
        return userRoles;
    }

    @GetMapping("/indexBen")
    public String benevoleHoraireList(@AuthenticationPrincipal CustomUserDetail principal,
            @ModelAttribute(BENEVOLE) BenevoleRequest request, Model model) {
        Benevole benevole = bs.one(principal.getId()).orElse(null);
        if (benevole != null) {
            model.addAttribute(HORAIRES,
                    benevole.getHoraires().stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return "horaire/benevole";
        } else {
            model.addAttribute(HORAIRES, null);
            return "horaire/benevole";
        }
    }

    @GetMapping("/indexBen/create")
    public String benevoleHoraireCreate(@AuthenticationPrincipal CustomUserDetail principal,
            @ModelAttribute(BENEVOLE) BenevoleRequest request, Model model) {
        Benevole benevole = bs.one(principal.getId()).orElse(null);
        if (benevole != null) {
            request.setHoraires(benevole.getHoraires());
            model.addAttribute("hd",
                    hs.availableAfterDayAndType(DAYS, "Distribution").stream().map(HoraireResponse::new)
                            .collect(Collectors.toList()));
            model.addAttribute("hr",
                    hs.availableAfterDayAndType(DAYS, "Recuperation").stream().map(HoraireResponse::new)
                            .collect(Collectors.toList()));
            model.addAttribute(BENEVOLE, request);
            return "horaire/benevoleCreate";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/indexBen/store")
    public String benevoleHoraireStore(@AuthenticationPrincipal CustomUserDetail principal,
            @ModelAttribute(BENEVOLE) BenevoleRequest request, Model model) {
        Benevole benevole = bs.one(principal.getId()).orElse(null);
        if (benevole != null) {
            for (Horaire h : request.getHoraires()) {
                benevole.addHoraire(h);
            }
            benevole = bs.addOrUpdate(benevole);
            model.addAttribute(HORAIRES,
                    benevole.getHoraires().stream().map(HoraireResponse::new).collect(Collectors.toList()));
            return REDIRECT_BENEVOLES_INDEX_BEN;
        } else {
            model.addAttribute(HORAIRES, null);
            return REDIRECT_BENEVOLES_INDEX_BEN;
        }
    }

    @GetMapping("/{id}/indexBen/delete")
    public String benevoleHoraireDestroy(@AuthenticationPrincipal CustomUserDetail principal,
            @ModelAttribute(BENEVOLE) BenevoleRequest request,
            @PathVariable("id") Long id, Model model) {
        Benevole benevole = bs.one(principal.getId()).orElse(null);
        if (benevole != null) {
            Horaire h = hs.one(id).orElse(null);
            if (h != null) {
                benevole.removeHoraire(h);
                bs.addOrUpdate(benevole);
            }
            return REDIRECT_BENEVOLES_INDEX_BEN;
        } else {
            model.addAttribute(HORAIRES, null);
            return REDIRECT_BENEVOLES_INDEX_BEN;
        }
    }

    @GetMapping("/{id}/missions")
    public String missions(@PathVariable("id") Long id, Model model) {
        Benevole b = bs.one(id).orElse(null);
        model.addAttribute("equipes", b.getEquipes().stream().map(EquipeResponse::new).collect(Collectors.toList()));
        return "benevole/missions";
    }
}
