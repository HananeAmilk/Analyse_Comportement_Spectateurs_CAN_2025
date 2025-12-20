package com.example.analyse_comportement_spectateurs.model.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Database Entity
@Entity
@Table(name = "spectators")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spectator {
    @Id
    private String spectatorId;
    private Integer age;
    private String nationality;
    private Integer totalMatches;
    private String category; // Behavioral classification

}
