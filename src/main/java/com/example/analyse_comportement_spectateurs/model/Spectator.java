package com.example.analyse_comportement_spectateurs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Database Entity
@Entity
@Table(name = "spectators")
public class Spectator {
    @Id
    private String spectatorId;
    private Integer age;
    private String nationality;
    private Integer totalMatches;
    private String category; // Behavioral classification

    public String getSpectatorId() {
        return spectatorId;
    }

    public void setSpectatorId(String spectatorId) {
        this.spectatorId = spectatorId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
