/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.services;

import com.example.entities.Benevole;
import com.example.entities.Notification;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.repositories.NotificationRepository;
import com.example.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yves
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository nr;

    @Autowired
    private RoleRepository rr;

    /**
     * Méthode de lecture des notifs en BD
     * 
     * @param user Proprietaire des données sauvegardées
     * 
     * @return La liste des notifications de l'utilisateur indexé.
     */
    public List<Notification> all(Benevole user) {
        return nr.getUserNotifications(user);
    }

    /**
     * Méthode de recherche d'une donnée en BD
     * 
     * @param id Identifiant de la donnée à rechercher
     * 
     * @return Un optionel de la donnée, vide ou non.
     */
    public Optional<Notification> one(Long id) {
        return nr.findById(id);
    }

    /**
     * Méthode d'ajout ou de modification d'une donnée en BD
     * 
     * @param data L'objet à ajouter ou à modifier
     * 
     * @return L'objet nouvellement ajouté ou modifié.
     */
    public Notification addOrUpdate(Notification data) {
        return nr.saveAndFlush(data);
    }

    /**
     * Méthode de suppression d'une donnée en BD
     * 
     * @param id Identifiant de l'objet à supprimer
     * 
     * @return Une chaine de charactères indiquant si l'opération s'est bien terminée ou pas.
     */
    public String delete(Long id) {
        Notification data = this.one(id).orElse(null);
        if (data != null) {
            nr.deleteById(id);
            return "Notification deleted!";
        }
        return String.format("Notification with id %d does not exist!", id);
    }

    /**
     * Méthode de notification en masse de tous les utilisateurs du système possedant le role "ADMIN"
     * 
     * @param message Contenu de la notification à diffuser
     */
    public void notifyAdmins(String message) {
        Role role = rr.findById(2L).orElse(null);
        if (role != null) {
            List<User> admins = role.getUsers();
            admins.forEach(admin -> this.addOrUpdate(new Notification(message, admin.getBenevole())));
        }
    }

    /**
     * Méthode de notification en masse de tous les utilisateurs du système possedant le role "BENEVOLE"
     * 
     * @param message Contenu de la notification à diffuser
     */
    public void notifyBenevoles(String message) {
        Role role = rr.findById(1L).orElse(null);
        if (role != null) {
            List<User> benevoles = role.getUsers();
            benevoles.forEach(ben -> this.addOrUpdate(new Notification(message, ben.getBenevole())));
        }
    }
}
