/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Distribution;
import com.example.repositories.DistributionRepository;
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
public class DistributionService {
    
    @Autowired
    private DistributionRepository dr;
    
    public List<Distribution> all(Boolean etat) {
        return dr.getDistributionsByEtat(etat);
    }
    
    public List<Distribution> all(String statut) {
        return dr.getDistributionsByStatut(statut);
    }
    
    public List<Distribution> allByDates(LocalDateTime start, LocalDateTime end) {
        return dr.getDistributionsByStartAndEnd(start, end);
    }
    
    public Optional<Distribution> one(Long id) {
        return dr.findById(id);
    }
    
    public Distribution addOrUpdate(Distribution data) {
        return dr.saveAndFlush(data);
    }
    
    public String delete(Long id) {
        Distribution data = this.one(id).orElse(null);
        if (data != null) {
            data.setEtat(false);
            this.addOrUpdate(data);
            return "Resource deleted!";
        }
        return String.format("Resource with id %d does not exist!", id);
    }
}
