/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Yves
 */
@Entity
@Table(name = "horaires")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Horaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horaire")
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "type")
    private String type;

    @Column(name = "color")
    private String color;

    @Column(name = "etat")
    private boolean etat = true;

    @JoinTable(name = "planifier", joinColumns = @JoinColumn(name = "id_horaire"), inverseJoinColumns = @JoinColumn(name = "id_ben"))
    @ManyToMany
    private List<Benevole> benevoles = new ArrayList<>();

    public void addBenevole(Benevole b){
        if (!this.benevoles.contains(b)) {
            this.benevoles.add(b);
            b.addHoraire(this);
        }
    }
    
    public void removeBenevole(Benevole b){
        if (this.benevoles.contains(b)) {
            this.benevoles.remove(b);
            b.removeHoraire(this);
        }
    }
    
    @Override
    public String toString() {
        return "Horaire{" + "id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", type=" + type + ", color=" + color + ", etat=" + etat + '}';
    }

}
