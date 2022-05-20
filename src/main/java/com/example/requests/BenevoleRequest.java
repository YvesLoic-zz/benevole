/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.requests;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.entities.Horaire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Class servant à la validation des informations des champs dsans les formulaires
 * 
 * @author Yves
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BenevoleRequest implements Serializable {

    private Long id;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String username;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String name;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String lastName;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 50)
    private String address;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 6)
    private String code;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 15)
    private String ville;

    @NotEmpty(message = "{mandatoryField}")
    @Email(message = "{invalidEmail}")
    private String email;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 9, max = 15)
    private String phone;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 5, max = 16)
    private String password;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String profession;

    @NotEmpty(message = "{mandatoryField}")
    private String sexe;

    @NotEmpty(message = "{mandatoryField}")
    private String dateNaiss;

    @NotNull(message = "{mandatoryField}")
    private boolean permis;

    @NotNull(message = "{mandatoryField}")
    private boolean chef;

    @Min(value = 1, message = "{minValue(1)}")
    private Long idRole;

    private List<Horaire> horaires;
}
