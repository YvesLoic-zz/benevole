/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Categorie;
import com.example.entities.Magasin;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yves
 */
@Repository
public interface MagasinRepository extends JpaRepository<Magasin, Long> {

    @Query("select m from Magasin m where m.etat = :etat")
    public List<Magasin> getMagasinByEtat(@Param("etat") boolean etat);

    @Query("select m from Magasin m where m.phone = :phone")
    public Optional<Magasin> findByPhone(@Param("phone") String phone);

    @Query("select m from Magasin m where m.categorie = :c and m.etat = :etat")
    public List<Magasin> getMagasinByCategorieAndEtat(@Param("c") Categorie c, @Param("etat") boolean etat);
}
