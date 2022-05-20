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
public class CategorieRequest implements Serializable {

    private Long id;

    @NotEmpty(message = "mandatoryField")
    @Length(message = "{nameLength}", min = 3, max = 25)
    private String name;
}
