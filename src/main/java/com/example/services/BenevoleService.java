/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Benevole;
import com.example.repositories.BenevoleRepository;
import com.example.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class BenevoleService {

    @Autowired
    private BenevoleRepository br;

    @Autowired
    private UserRepository ur;

    public List<Benevole> all(boolean etat) {
        return br.getBenevolesByEtat(etat);
    }

    public Optional<Benevole> one(Long id) {
        return br.findById(id);
    }

    public Benevole addOrUpdate(Benevole data) {
        return br.saveAndFlush(data);
    }

    public String delete(Long id) {
        Benevole data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            data.getUsername().setEnabled(false);
            ur.saveAndFlush(data.getUsername());
            return "Resource deleted!";
        }
        return String.format("Resource with id %d does not exist!", id);
    }

    /**
     * Méthode de vérification de doublon sur l'email des bénévoles
     * 
     * @param email Valeur unique à vérifier en BD
     * @param bene Optionel bénévole, possesseur d'un email
     * 
     * @return True si l'email testé est deja utilisé et False dans le cas contraire.
     */
    public boolean emailExist(String email, Optional<Benevole> bene) {
        boolean test = false;
        Benevole b = br.getUserByEmail(email).orElse(null);
        if (b != null) {
            test = true;
            if (!bene.isEmpty() && !b.getUsername().getUsername().equals(bene.get().getUsername().getUsername())) {
                test = false;
            }
        }
        return test;
    }

    /**
     * Méthode de vérification de doublon sur le téléphone des bénévoles
     * 
     * @param phone Valeur unique à vérifier en BD
     * @param bene Optionel bénévole, possesseur d'un numéro de téléphone
     * 
     * @return True si le numéro de téléphone testé est deja utilisé et False dans le cas contraire.
     */
    public boolean phoneExist(String phone, Optional<Benevole> bene) {
        boolean test = false;
        Benevole b = br.getUserByPhone(phone).orElse(null);
        if (b != null) {
            test = true;
            if (!bene.isEmpty() && !b.getUsername().getUsername().equals(bene.get().getUsername().getUsername())) {
                test = false;
            }
        }
        return test;
    }

}
