package com.example;

import com.example.entities.Benevole;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.repositories.BenevoleRepository;
import com.example.repositories.RoleRepository;
import com.example.repositories.UserRepository;
import java.time.LocalDate;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class BenevoleApplication {

    private static final String ADMIN_ATTR = "admin";

    @Autowired
    private BenevoleRepository benevoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(BenevoleApplication.class, args);
    }

    /**
     * Méthode d'initialisation de la BD.
     * Elle est exécutée au lancement de l'appli mais ne remplie la BD que lorsque cette dernière est nouvellement crée
     * 
     * @return null Utilisée de telle sorte qu'elle ne retourne aucun résultat
     */
    @Bean
    public CommandLineRunner commandLineRunner() {
        List<Role> initialRoles = roleRepository.findAll();
        if (initialRoles.isEmpty()) {
            initialRoles = Arrays.asList(
                    new Role("ROLE_BENEVOLE"),
                    new Role("ROLE_ADMIN")
            );
            initialRoles = roleRepository.saveAll(initialRoles);
            User admin = new User(ADMIN_ATTR, encoder.encode(ADMIN_ATTR));
            admin = userRepository.save(admin);
            Benevole adm = new Benevole(ADMIN_ATTR, "adminlast", LocalDate.of(1998, 9, 1), "admin@gmail.com", "Maison", "698586208", true, "administrateur", "Masculin", admin);
            adm.setPassword(ADMIN_ATTR);
            admin.addRole(initialRoles);
            benevoleRepository.save(adm);
            userRepository.saveAndFlush(admin);
            log.info("Initialisation de la BD OKAY!");
        }
        return null;
    }

}
