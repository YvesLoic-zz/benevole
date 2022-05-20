/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Equipe;
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
public class EquipeResponse implements Serializable {

    private Long id;
    private String name;
    private LocalDateTime dateCreation;
    private LocalDateTime start;
    private LocalDateTime end;
    private String chef;
    private String type;
    private String categorie;
    private int members;

    public EquipeResponse(Equipe e) {
        this.id = e.getId();
        this.start = e.getStart();
        this.end = e.getEnd();
        this.name = e.getName();
        this.dateCreation = e.getCreatedAt();
        this.chef = e.getBenevole() != null ? e.getBenevole().getName().concat(" ").concat(e.getBenevole().getLastName()) : null;
        this.members = e.getMembers().size();
        this.type = e.getType();
        this.categorie = e.getCategorie().getName();
    }
}
