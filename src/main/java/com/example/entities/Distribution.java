/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.entities;

import com.example.util.Statut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yves
 */
@Entity
@Table(name = "sceances_distributions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Distribution implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrib")
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "dist_start")
    private LocalDateTime start;

    @Column(name = "dist_end")
    private LocalDateTime end;

    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String statut = Statut.EN_ATTENTE.name();
    
    @Column(name = "rapport")
    private String rapport;

    @Column(name = "etat")
    private Boolean etat = true;

    @JoinColumn(name = "equipe", referencedColumnName = "id_equipe")
    @OneToOne(optional = false)
    private Equipe equipe;

    @Override
    public String toString() {
        return "Distribution{" + "id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", location=" + location + ", statut=" + statut + ", etat=" + etat + ", equipe=" + equipe + '}';
    }

}
