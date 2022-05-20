/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entities.Benevole;
import com.example.entities.Equipe;
import com.example.entities.Horaire;
import com.example.entities.Magasin;
import com.example.entities.Recuperation;
import com.example.responses.BenevoleResponse;
import com.example.responses.EquipeResponse;
import com.example.responses.MagasinResponse;
import com.example.services.EquipeService;
import com.example.services.HoraireService;
import com.example.services.RecuperationService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Yves
 */
@RestController
public class Helper {

    @Autowired
    private EquipeService es;

    @Autowired
    private HoraireService hs;
    
    @Autowired
    private RecuperationService rs;

    @GetMapping("/getEquipes/{id}")
    public List<EquipeResponse> horaireEquipe(@PathVariable("id") Long id) {
        Horaire h = hs.one(id).orElse(null);
        List<Equipe> e = es.allByDates(h.getStartDate(), h.getEndDate());
        return e.stream().map(EquipeResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/getUsers/{id}")
    public List<BenevoleResponse> horaireBen(@PathVariable("id") Long id) {
        Horaire h = hs.one(id).orElse(null);
        List<Equipe> e = es.allByDates(h.getStartDate(), h.getEndDate());
        List<Benevole> availables = new ArrayList<>(h.getBenevoles());
        e.forEach(equipe -> {
            availables.removeAll(equipe.getMembers());
        });
        return availables.stream().map(BenevoleResponse::new).collect(Collectors.toList());
    }
    
    @GetMapping("/equipe/{id}")
    public List<MagasinResponse> magasins(@PathVariable("id") Long id){
        Equipe e = es.one(id).orElse(null);
        List<Magasin> availables = new ArrayList<>(e.getCategorie().getMagasins());
        List<Recuperation> r = rs.allByDates(e.getStart(), e.getEnd());
        r.forEach(recuperation -> {
            availables.removeAll(recuperation.getMagasins());
        });
        return availables.stream().map(MagasinResponse::new).collect(Collectors.toList());
    }
}
