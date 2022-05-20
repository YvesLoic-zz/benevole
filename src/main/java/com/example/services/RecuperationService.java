/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Recuperation;
import com.example.repositories.RecuperationRepository;
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
public class RecuperationService {

    @Autowired
    private RecuperationRepository rr;

    /**
     * Méthode de lecture des scéances de récupérations en BD
     * 
     * @param etat Valeur servant de filtre, true pour actif et false dans le cas contraire
     * 
     * @return La liste des scéances de récupérations selon le paramètre indiqué.
     */
    public List<Recuperation> all(Boolean etat) {
        return rr.getRecuperationsByEtat(etat);
    }

    /**
     * Méthode de lecture des scéances de récupération par dates
     * 
     * @param start Date de début
     * @param end Date de fin
     * 
     * @return La liste des récupérations selon les paramètres renseignés.
     */
    public List<Recuperation> allByDates(LocalDateTime start, LocalDateTime end) {
        return rr.getRecuperationByStartAndEnd(start, end);
    }
    
    /**
     * Méthode de recherche d'une donnée en BD
     * 
     * @param id Identifiant de la donnée à rechercher
     * 
     * @return L'objet retrouvé
     */
    public Optional<Recuperation> one(Long id) {
        return rr.findById(id);
    }

    /**
     * Méthode d'ajout ou de modification d'une donnée en BD
     * 
     * @param data l'objet à ajouter ou à modifier en BD
     * 
     * @return L'objet nouvellement crée ou modifié.
     */
    public Recuperation addOrUpdate(Recuperation data) {
        return rr.saveAndFlush(data);
    }

    /**
     * Méthode de suppression d'une donnée en BD
     * 
     * @param id Identifiant de la donnée à supprimer
     * 
     * @return Une chaine de charactères indiquant si l'opération s'est bien terminée ou pas.
     */
    public String delete(Long id) {
        Recuperation data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Resource deleted!";
        }
        return String.format("Resource with id %d does not exist!", id);
    }
}
