/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.entities.Role;
import com.example.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Yves
 */
public class CustomUserDetail implements UserDetails {

    private final String appname;

    private final String autor;

    private final User user;

    public CustomUserDetail(User user, String name, String auteur) {
        this.user = user;
        this.appname = name;
        this.autor = auteur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (roles.size() == 1) {
            authorities.add(new SimpleGrantedAuthority(roles.get(0).getName()));
        } else {
            authorities.add(new SimpleGrantedAuthority(roles.get(1).getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getAppname() {
        return this.appname;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getBenevole().getId();
    }

    public String getAutor() {
        return this.autor;
    }
}
