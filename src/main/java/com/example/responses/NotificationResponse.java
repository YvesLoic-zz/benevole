/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.responses;

import com.example.entities.Notification;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Yves
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationResponse implements Serializable {

    private Long id;
    private String message;
    private LocalDateTime dateCreation;
    private boolean open;
    private boolean etat;

    public NotificationResponse(Notification n) {
        this.id = n.getId();
        this.message = n.getMessage();
        this.dateCreation = n.getCreatedAt();
        this.open = n.isOpen();
    }
}
