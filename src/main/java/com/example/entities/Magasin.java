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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Yves
 */
@Entity
@Table(name = "magasins")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Magasin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_magasin")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "etat")
    private Boolean etat = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "categorie", referencedColumnName = "id_categorie")
    @ManyToOne(optional = false)
    private Categorie categorie;

    @ManyToMany(mappedBy = "magasins")
    private List<Recuperation> recuperations = new ArrayList<>();

    @Override
    public String toString() {
        return "Magasin{" + "id=" + id + ", name=" + name + ", adresse=" + address + ", phone=" + phone + ", etat=" + etat + ", createdAt=" + createdAt + ", categorie=" + categorie + '}';
    }

    public void addRecuperation(Recuperation r){
        if (!this.recuperations.contains(r)) {
            this.recuperations.add(r);
            r.addMagasin(this);
        }
    }
}
