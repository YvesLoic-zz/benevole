/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Categorie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yves
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long>{
    
    @Query("select c from Categorie c where c.etat = :etat")
    public List<Categorie> getCategoriesByEtat(@Param("etat") boolean etat);
}
