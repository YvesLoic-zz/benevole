/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Benevole;
import com.example.entities.Equipe;
import com.example.repositories.EquipeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class EquipeService {

    @Autowired
    private EquipeRepository er;

    public List<Equipe> all(boolean etat) {
        return er.getEquipesByEtat(etat);
    }

    public List<Equipe> all(String type) {
        return er.getEquipesByType(type);
    }
    
    public List<Equipe> allByUserAndCat(Benevole b) {
        return er.getEquipesByUserAndEtat(b);
    }
    
    public List<Equipe> allByDates(LocalDateTime start, LocalDateTime end) {
        return er.getEquipesByStartAndEnd(start, end);
    }

    public Optional<Equipe> one(Long id) {
        return er.findById(id);
    }

    public Equipe addOrUpdate(Equipe data) {
        return er.saveAndFlush(data);
    }

    public String delete(Long id) {
        Equipe data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Equipe deleted!";
        }
        return String.format("Equipe with id %d does not exist!", id);
    }
}
