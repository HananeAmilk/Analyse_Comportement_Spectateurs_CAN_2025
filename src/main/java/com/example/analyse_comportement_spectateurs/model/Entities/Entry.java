package com.example.analyse_comportement_spectateurs.model.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spectator_id", nullable = false, length = 50)
    private String spectatorId;

    @Column(name = "match_id", nullable = false, length = 50)
    private String matchId;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "gate", nullable = false, length = 50)
    private String gate;

    @Column(name = "ticket_number", unique = true, nullable = false, length = 100)
    private String ticketNumber;

    @Column(name = "ticket_type", nullable = false, length = 50)
    private String ticketType;

    @Column(name = "tribune", length = 50)
    private String tribune;

    @Column(name = "bloc", length = 10)
    private String bloc;

    @Column(name = "rang")
    private Integer rang;

    @Column(name = "siege")
    private Integer siege;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}