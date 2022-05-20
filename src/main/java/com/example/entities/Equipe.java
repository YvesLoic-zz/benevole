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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Yves
 */
@Entity
@Table(name = "equipes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Equipe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipe")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @JoinColumn(name = "categorie", referencedColumnName = "id_categorie")
    @ManyToOne(optional = false)
    private Categorie categorie;

    @Column(name = "type")
    private String type;

    @Column(name = "etat")
    private Boolean etat = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinTable(name = "members", joinColumns = @JoinColumn(name = "id_equipe"), inverseJoinColumns = @JoinColumn(name = "id_ben"))
    @ManyToMany
    private List<Benevole> members = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "equipe")
    private Distribution distribution;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "equipe")
    private Recuperation recuperation;

    @OneToOne
    private Benevole benevole = null;

    public void addMember(Benevole b){
        if (!this.members.contains(b)) {
            this.members.add(b);
            b.addEquipe(this);
        }
    }
    
    public void removeMember(Benevole b){
        if (this.members.contains(b)) {
            this.members.remove(b);
            b.removeEquipe(this);
        }
    }
    
    public void addMembers(List<Benevole> members){
        this.members.addAll(members);
    }
    
    public void removeMembers(){
        this.members.clear();
    }
    
    @Override
    public String toString() {
        return "Equipe{" + "id=" + id + ", name=" + name + ", etat=" + etat + ", createdAt=" + createdAt + ", benevole=" + benevole + ", categorie=" + categorie + '}';
    }

}
