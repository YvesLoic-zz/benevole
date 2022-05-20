/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repositories;

import com.example.entities.Benevole;
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
public interface BenevoleRepository extends JpaRepository<Benevole, Long>  {
    
    @Query("SELECT b FROM Benevole b WHERE b.email = :email")
    public Optional<Benevole> getUserByEmail(@Param("email") String email);
    
    @Query("SELECT b FROM Benevole b WHERE b.phone = :phone")
    public Optional<Benevole> getUserByPhone(@Param("phone") String phone);
    
    @Query("select b from Benevole b where b.etat = :etat")
    public List<Benevole> getBenevolesByEtat(@Param("etat") boolean etat);
    
    @Query("select b from Benevole b where b.chef = :chef and b.etat = :etat")
    public List<Benevole> getBenevolesTypeAndEtat(@Param("chef") boolean chef, @Param("etat") boolean etat);
}
