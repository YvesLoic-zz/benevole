/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Benevole;
import java.io.Serializable;
import java.time.LocalDate;
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
public class BenevoleResponse implements Serializable {

    private Long id;
    private String name;
    private String lastName;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String address;
    private String code;
    private String ville;
    private String phone;
    private boolean permis;
    private LocalDateTime created_at;
    private String profession;
    private String sexe;
    private UserResponse username;
    private int missions;
    private boolean chef;

    public BenevoleResponse(Benevole b) {
        this.id = b.getId();
        this.name = b.getName();
        this.lastName = b.getLastName();
        this.fullName = b.getName().concat(" ").concat(b.getLastName());
        this.birthDate = b.getBirthDate();
        this.email = b.getEmail();
        this.address = b.getAddress();
        this.code = b.getCode();
        this.ville = b.getVille();
        this.phone = b.getPhone();
        this.permis = b.isPermis();
        this.created_at = b.getCreated_at();
        this.profession = b.getProfession();
        this.sexe = b.getSexe();
        this.username = new UserResponse(b.getUsername());
        this.missions = b.getEquipes().size();
        this.chef = b.isChef();
    }

}
