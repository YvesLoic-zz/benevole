/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Magasin;
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
public class MagasinResponse implements Serializable {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private LocalDateTime dateCreation;
    private String categorie;

    public MagasinResponse(Magasin m) {
        this.id = m.getId();
        this.name = m.getName();
        this.address = m.getAddress();
        this.phone = m.getPhone();
        this.dateCreation = m.getCreatedAt();
        this.categorie = m.getCategorie().getName();
    }
}
