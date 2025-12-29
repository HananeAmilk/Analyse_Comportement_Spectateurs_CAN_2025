package com.example.analyse_comportement_spectateurs.model.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_name", unique = true, nullable = false, length = 100)
    private String statName;

    @Column(name = "stat_value", nullable = false, columnDefinition = "TEXT")
    private String statValue;

    @Column(name = "stat_type", nullable = false, length = 50)
    private String statType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }
}
