/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class UserService {

    @Autowired
    private UserRepository ur;

    /**
     * Méthode de recherche d'un compte utilisateur
     * 
     * @param id L'identifiant de l'utilisateur recherché
     * 
     * @return Un optionel d'utilisateur, objet pouvant etre vide ou non. 
     */
    public Optional<User> one(String id) {
        return ur.findById(id);
    }

    /**
     * Méthode d'ajout ou de mise à jour d'un compte utilisateur
     * 
     * @param user L'objet utilisateur à créer ou à modifier
     * 
     * @return L'objet nouvellement crée ou modifié.
     */
    public User addOrUpdate(User user) {
        return ur.saveAndFlush(user);
    }

    /**
     * Méthode de vérification de doublon sur le username d'un compte
     * 
     * @param username Valeur unique à vérifier en BD
     * @param u Optionel d'utilisateur, possesseur d'un username
     * 
     * @return True si le username testé est deja utilisé et False dans le cas contraire.
     */
    public boolean usernameExist(String username, Optional<User> u) {
        boolean test = false;
        User user = ur.getUserByUsername(username).orElse(null);
        if (user != null) {
            test = true;
            if (!u.isEmpty() && !user.getUsername().equals(u.get().getUsername())) {
                test = false;
            }

        }
        return test;
    }
}
