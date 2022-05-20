/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Distribution;
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
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Query("select d from Distribution d where d.etat = :etat")
    public List<Distribution> getDistributionsByEtat(@Param("etat") boolean etat);

    @Query("select d from Distribution d where d.statut = :statut")
    public List<Distribution> getDistributionsByStatut(@Param("statut") String statut);
    
    @Query("select d from Distribution d where d.startDate = :start and d.endDate = :end")
    public List<Distribution> getDistributionsByStartAndEnd(@Param("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @Param("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end);
}
