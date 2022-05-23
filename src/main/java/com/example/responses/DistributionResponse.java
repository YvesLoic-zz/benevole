/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Distribution;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Yves
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DistributionResponse implements Serializable {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String lieux;
    private String statut;
    private EquipeResponse equipe;
    private int effectif;
    private String rapport;
    private LocalDateTime start;
    private LocalDateTime end;

    public DistributionResponse(Distribution d) {
        this.id = d.getId();
        this.startDate = d.getStartDate();
        this.endDate = d.getEndDate();
        this.lieux = d.getLocation();
        this.statut = d.getStatut();
        this.equipe = new EquipeResponse(d.getEquipe());
        this.effectif = d.getEquipe().getMembers().size();
        this.rapport = d.getRapport();
        this.start = d.getStart();
        this.end = d.getEnd();
    }
}
