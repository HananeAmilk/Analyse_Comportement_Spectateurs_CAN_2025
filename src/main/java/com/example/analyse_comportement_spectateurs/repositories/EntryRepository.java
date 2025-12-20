package com.example.analyse_comportement_spectateurs.repositories;

import com.example.analyse_comportement_spectateurs.model.Entities.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    /**
     * Compte le nombre d'entrées par spectateur
     */
    @Query("SELECT e.spectatorId, COUNT(e) FROM Entry e GROUP BY e.spectatorId")
    List<Object[]> countEntriesBySpectator();

    /**
     * Trouve toutes les entrées d'un spectateur
     */
    List<Entry> findBySpectatorId(String spectatorId);

    /**
     * Compte les entrées par match
     */
    @Query("SELECT e.matchId, COUNT(e) FROM Entry e GROUP BY e.matchId")
    List<Object[]> countEntriesByMatch();

    /**
     * Compte les entrées par type de ticket
     */
    @Query("SELECT e.ticketType, COUNT(e) FROM Entry e GROUP BY e.ticketType")
    List<Object[]> countByTicketType();

    /**
     * Compte les entrées par porte d'accès
     */
    @Query("SELECT e.gate, COUNT(e) FROM Entry e GROUP BY e.gate")
    List<Object[]> countByGate();

    /**
     * Compte les entrées par tribune
     */
    @Query("SELECT e.tribune, COUNT(e) FROM Entry e WHERE e.tribune IS NOT NULL GROUP BY e.tribune")
    List<Object[]> countByTribune();

    /**
     * Trouve les entrées par match
     */
    List<Entry> findByMatchId(String matchId);

    /**
     * Vérifie si un ticket existe déjà
     */
    boolean existsByTicketNumber(String ticketNumber);

    /**
     * Temps moyen d'arrivée (requête personnalisée selon logique métier)
     */
    @Query("SELECT AVG(HOUR(e.entryTime)) FROM Entry e")
    Double getAverageEntryHour();

    /**
     * Temps minimum et maximum d'arrivée
     */
    @Query("SELECT MIN(e.entryTime), MAX(e.entryTime) FROM Entry e")
    List<Object[]> getMinMaxEntryTimes();
}