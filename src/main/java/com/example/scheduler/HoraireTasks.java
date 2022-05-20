/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.scheduler;

import com.example.entities.Horaire;
import com.example.services.HoraireService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Yves
 */
@Component
public class HoraireTasks {

    @Autowired
    private HoraireService hs;

    private final LocalDateTime now = LocalDateTime.now();

    /**
     * Méthode de vérification de la validité des horaires disponibles en BD
     */
    @Scheduled(fixedRate = 60000)
    public void disableHoraire() {
        List<Horaire> h = hs.all(true);
        for (Horaire horaire : h) {
            if (now.isAfter(horaire.getEndDate())) {
                horaire.setEtat(false);
                hs.addOrUpdate(horaire);
            }
        }
    }
}
