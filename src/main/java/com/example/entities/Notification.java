/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Yves
 */
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notif")
    private Long id;

    @Column(name = "content")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "open")
    private boolean open = false;

    @JoinColumn(name = "benevole", referencedColumnName = "id_ben")
    @ManyToOne(optional = false)
    private Benevole benevole;

    public Notification(String message, Benevole benevole) {
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.benevole = benevole;
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", message=" + message + ", createdAt=" + createdAt + ", open=" + open + ", benevole=" + benevole + '}';
    }

}
