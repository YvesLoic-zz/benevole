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

/**
 *
 * @author Yves
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoraireRequest implements Serializable {

    private Long id;
    
    @NotEmpty(message = "{mandatoryField}")
    private String start;

    @NotEmpty(message = "{mandatoryField}")
    private String end;
    
    @NotEmpty(message = "{mandatoryField}")
    private String type;
    
    private String color;
}
