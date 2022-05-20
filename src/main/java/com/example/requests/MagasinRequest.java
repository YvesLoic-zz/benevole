/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.requests;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author Yves
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MagasinRequest implements Serializable {
    
    private Long id;
    
    private Long categorie;

    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String name;
    
    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 3, max = 50)
    private String address;
    
    @NotEmpty(message = "{mandatoryField}")
    @Length(message = "{nameLength}", min = 9, max = 15)
    private String phone;
}
