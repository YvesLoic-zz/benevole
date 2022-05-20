/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Categorie;
import com.example.entities.Magasin;
import com.example.repositories.MagasinRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class MagasinService {

    @Autowired
    private MagasinRepository mr;

    /**
     * Méthode de lecture des magasins par catégorie
     * 
     * @param c La catégorie à rechercher en BD
     * @param etat True ou False selon l'état des magasins à extraire en BD
     * 
     * @return La liste des magasins selon la categorie et l'état donné.
     */
    public List<Magasin> all(Categorie c, boolean etat) {
        return mr.getMagasinByCategorieAndEtat(c, etat);
    }
    
    public List<Magasin> all(boolean etat) {
        return mr.getMagasinByEtat(etat);
    }

    public Optional<Magasin> one(Long id) {
        return mr.findById(id);
    }

    public Magasin addOrUpdate(Magasin data) {
        return mr.saveAndFlush(data);
    }

    public String delete(Long id) {
        Magasin data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Magasin deleted!";
        }
        return String.format("Magasin with id %d does not exist!", id);
    }
}
