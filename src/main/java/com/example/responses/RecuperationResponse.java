/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Recuperation;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
public class RecuperationResponse implements Serializable {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String statut;
    private boolean etat;
    private EquipeResponse equipe;
    private int nbMag;
    private int effectif;
    private List<MagasinResponse> magasins;
    private String rapport;
    private LocalDateTime start;
    private LocalDateTime end;

    public RecuperationResponse(Recuperation r) {
        this.id = r.getId();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.statut = r.getStatus();
        this.etat = r.isEtat();
        this.equipe = new EquipeResponse(r.getEquipe());
        this.nbMag = r.getMagasins().size();
        this.effectif = r.getEquipe().getMembers().size();
        this.magasins = r.getMagasins().stream().map(MagasinResponse::new).collect(Collectors.toList());
        this.rapport = r.getRapport();
        this.start = r.getStart();
        this.end = r.getEnd();
    }
}
