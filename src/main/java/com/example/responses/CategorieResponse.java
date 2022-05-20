/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Categorie;
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
public class CategorieResponse implements Serializable{
    
    private Long id;
    private String name;
    private int equipes;
    private int magasins;
    private LocalDateTime createdAt;

    public CategorieResponse(Categorie c) {
        this.id = c.getId();
        this.name = c.getName();
        this.equipes = c.getEquipes().size();
        this.magasins = c.getMagasins().size();
        this.createdAt = c.getCreatedAt();
    }
    
}
