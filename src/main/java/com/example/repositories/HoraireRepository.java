/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Horaire;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yves
 */
@Repository
public interface HoraireRepository extends JpaRepository<Horaire, Long> {

        @Query("select h from Horaire h where h.etat = :etat")
        public List<Horaire> getHorairesByEtat(@Param("etat") boolean etat);

        @Query("select h from Horaire h where h.type = :type and h.etat = true")
        public List<Horaire> getHorairesByType(@Param("type") String type);

        @Query("select h from Horaire h where h.startDate like('%?1%') and h.etat = true")
        public List<Horaire> getHorairesByDate(String date);

        @Query("select h from Horaire h where not(h.endDate < :from or h.startDate > :to)")
        public List<Horaire> findBetween(@Param("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
                        @Param("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end);

        @Query("select h from Horaire h where h.startDate = :start and h.endDate = :end")
        public Horaire findDistinctByStartDateAndEndDate(
                        @Param("start") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
                        @Param("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end);

        // @Query(nativeQuery = true, value = "SELECT * FROM `horaires` WHERE start_date >= DATE(NOW() + INTERVAL :days DAY) AND type=:type")
        @Query("select h from Horaire h where h.startDate >= CURRENT_DATE + :days and h.type = :type")
        public List<Horaire> findByDaysAndType(@Param("days") Double days, @Param("type") String type);
}
