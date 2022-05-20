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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Yves
 */
@Entity
@Table(name = "sceances_recuperations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recuperation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recup")
    private Long id;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "recup_start")
    private LocalDateTime start;

    @Column(name = "recup_end")
    private LocalDateTime end;
    
    @Column(name = "status")
    private String status = Statut.EN_ATTENTE.name();
    
    @Column(name = "rapport")
    private String rapport;
    
    @Column(name = "etat")
    private boolean etat = true;
    
    @JoinColumn(name = "equipe", referencedColumnName = "id_equipe")
    @OneToOne(optional = false)
    private Equipe equipe;
    
    @JoinTable(name = "recuperations", joinColumns = @JoinColumn(name = "id_recup"), inverseJoinColumns = @JoinColumn(name = "id_magasin"))
    @ManyToMany
    private List<Magasin> magasins = new ArrayList<>();

    public void addMagasin(Magasin m){
        if (!this.magasins.contains(m)) {
            this.magasins.add(m);
            m.addRecuperation(this);
        }
    }
    
    public void removeMagasins(){
        this.magasins.clear();
    }
}
