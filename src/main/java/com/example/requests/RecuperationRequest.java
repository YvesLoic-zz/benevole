/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.requests;

import com.example.entities.Magasin;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Min;
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
public class RecuperationRequest implements Serializable {

    private Long id;
    
    private String status;
    
    @Min(value = 1, message = "{minValue(1)}")
    private Long equipe;

    private List<Magasin> magasins;

    @Min(value = 1, message = "{minValue(1)}")
    private Long horaire;

    private String rapport;
}
