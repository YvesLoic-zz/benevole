/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Horaire;
import com.example.repositories.HoraireRepository;
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
public class HoraireService {

    @Autowired
    private HoraireRepository hr;
    
    private static final String R_COLOR = "blue";
    private static final String D_COLOR = "green";

    public List<Horaire> all(LocalDateTime start, LocalDateTime end) {
        return hr.findBetween(start, end);
    }
    
    public Horaire findByStartDateAndEndDate(LocalDateTime start, LocalDateTime end){
        return hr.findDistinctByStartDateAndEndDate(start, end);
    }

    public List<Horaire> allByDate(String date) {
        return hr.getHorairesByDate(date);
    }

    /**
     * Méthode de lecture des horaires avant indisponibilité
     * 
     * @param days Le nombre de jours
     * @param type Le type de période désiré
     * 
     * @return La liste des horaires selon le type et le nombre de jours avant indisponibilité.
     */
    public List<Horaire> availableAfterDayAndType(Double days, String type) {
        return hr.findByDaysAndType(days, type);
    }

    /**
     * Méthode de lecture des horaire par type 
     * 
     * @param type Le type d'horaire(Recup/Distrib) à extraire en bd
     * 
     * @return la liste des horaires retrouvés en BD.
     */
    public List<Horaire> all(String type) {
        return hr.getHorairesByType(type);
    }

    public List<Horaire> all(boolean etat) {
        return hr.getHorairesByEtat(etat);
    }

    public Optional<Horaire> one(Long id) {
        return hr.findById(id);
    }

    public Horaire addOrUpdate(Horaire data) {
        data.setColor(data.getType().equals("Recuperation") ? R_COLOR : D_COLOR);
        return hr.saveAndFlush(data);
    }

    public String delete(Long id) {
        Horaire data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Resource deleted!";
        }
        return String.format("Resource with id %d does not exist!", id);
    }
}
