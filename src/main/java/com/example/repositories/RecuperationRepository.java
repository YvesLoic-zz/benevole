/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Recuperation;
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
public interface RecuperationRepository extends JpaRepository<Recuperation, Long> {

    @Query("select r from Recuperation r where r.etat = :etat")
    public List<Recuperation> getRecuperationsByEtat(@Param("etat") boolean etat);
    
    @Query("select r from Recuperation r where r.startDate = :start and r.endDate = :end")
    public List<Recuperation> getRecuperationByStartAndEnd(@Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);
}
