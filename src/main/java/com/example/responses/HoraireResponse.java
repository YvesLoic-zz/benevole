/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Horaire;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
public class HoraireResponse implements Serializable {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String type;
    private String color;
    private int benevoles;
    private List<BenevoleResponse> occupants;

    public HoraireResponse(Horaire h) {
        this.id = h.getId();
        this.start = h.getStartDate();
        this.end = h.getEndDate();
        this.type = h.getType();
        this.color = h.getColor();
        this.benevoles = h.getBenevoles().size();
        this.occupants = h.getBenevoles().stream().map(BenevoleResponse::new).collect(Collectors.toList());
    }
}
