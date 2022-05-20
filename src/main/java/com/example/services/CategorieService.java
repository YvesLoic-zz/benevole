/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Categorie;
import com.example.repositories.CategorieRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class CategorieService {
    
    @Autowired
    private CategorieRepository cr;

    public List<Categorie> all(boolean etat) {
        return cr.getCategoriesByEtat(etat);
    }

    public Optional<Categorie> one(Long id) {
        return cr.findById(id);
    }

    public Categorie addOrUpdate(Categorie data) {
        return cr.saveAndFlush(data);
    }

    public String delete(Long id) {
        Categorie data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Resource deleted!";
        }
        return String.format("Resource with id %d does not exist!", id);
    }
}
