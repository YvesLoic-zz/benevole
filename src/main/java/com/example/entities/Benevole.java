/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Yves
 */
@Entity
@Table(name = "benevoles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Benevole implements Serializable {
 
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ben")
    private Long id;

    @Column(name = "first_name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    @Past(message = "{passDate}")
    private LocalDate birthDate;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "adress")
    private String address;

    @Column(name = "postal_code")
    private String code;

    @Column(name = "city")
    private String ville;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "permis")
    private boolean permis = false;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "profession")
    private String profession;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "etat")
    private boolean etat = true;

    @Column(name = "chef")
    private boolean chef = false;

    @ManyToMany(mappedBy = "benevoles")
    private List<Horaire> horaires = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Equipe> equipes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "benevole")
    private List<Notification> notifications = new ArrayList<>();

    @JoinColumn(name = "username", referencedColumnName = "username")
    @OneToOne(optional = false)
    private User username;

    public Benevole(String name, String lastName, LocalDate birthDate, String email, String address, String phone, boolean permis, String profession, String sexe, User user) {
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.permis = permis;
        this.profession = profession;
        this.sexe = sexe;
        this.created_at = LocalDateTime.now();
        this.username = user;
    }

    public void addEquipe(Equipe e) {
        if (!this.equipes.contains(e)) {
            this.equipes.add(e);
            e.addMember(this);
        }
    }
    
    public void removeEquipe(Equipe e){
        if (this.equipes.contains(e)) {
            this.equipes.remove(e);
            e.removeMember(this);
        }
    }

    public void addHoraire(Horaire h){
        if (!this.horaires.contains(h)) {
            this.horaires.add(h);
            h.addBenevole(this);
        }
    }
    
    public void removeHoraire(Horaire h){
        if (this.horaires.contains(h)) {
            this.horaires.remove(h);
            h.removeBenevole(this);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Benevole other = (Benevole) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Benevole {address=" + address + ", chef=" + chef + ", code=" + code + ", email=" + email + ", etat="
                + etat + ", id=" + id + ", lastName=" + lastName + ", name=" + name + ", permis=" + permis + ", phone="
                + phone + ", profession=" + profession + ", sexe=" + sexe + ", ville=" + ville + "}";
    }


}
