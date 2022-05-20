/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Benevole;
import com.example.entities.Equipe;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yves
 */
@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    @Query("select e from Equipe e where e.etat = :etat")
    public List<Equipe> getEquipesByEtat(@Param("etat") boolean etat);

    @Query("select e from Equipe e where e.type = :type and e.etat = true")
    public List<Equipe> getEquipesByType(@Param("type") String type);
    
    @Query("select e from Equipe e where e.benevole = :b and e.etat = true")
    public List<Equipe> getEquipesByUserAndEtat(@Param("b") Benevole b);
    
    @Query("select e from Equipe e where e.start = :start and e.end = :end")
    public List<Equipe> getEquipesByStartAndEnd(@Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);

    @Query("select e from Equipe e where e.categorie = :categorie and e.etat = true")
    public List<Equipe> getEquipesByCategorieAndEtat(@Param("categorie") String categorie);
}
