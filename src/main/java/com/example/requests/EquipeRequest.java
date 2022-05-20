/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.requests;

import com.example.entities.Benevole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class EquipeRequest implements Serializable {

    private Long id;

    @Min(value = 1, message = "{minValue(1)}")
    @NotNull(message = "{mandatoryField}")
    private Long ben;

    @Min(value = 1, message = "{minValue(1)}")
    @NotNull(message = "{mandatoryField}")
    private Long categorie;

    @Min(value = 1, message = "{minValue(${1})}")
    @NotNull(message = "{mandatoryField}")
    private Long horaire;

    @NotEmpty(message = "{mandatoryField}")
    @Size(message = "{nameLength}", min = 3, max = 25)
    private String name;

    private List<Benevole> members = new ArrayList<>();
}
