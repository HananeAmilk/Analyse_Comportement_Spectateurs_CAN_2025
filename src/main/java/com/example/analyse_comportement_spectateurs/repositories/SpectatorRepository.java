package com.example.analyse_comportement_spectateurs.repositories;

import com.example.analyse_comportement_spectateurs.model.Entities.Spectator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpectatorRepository extends JpaRepository<Spectator, Long> {

    /**
     * Recherche un spectateur par son ID unique
     */
    Optional<Spectator> findBySpectatorId(String spectatorId);

    /**
     * Vérifie si un spectateur existe
     */
    boolean existsBySpectatorId(String spectatorId);

    /**
     * Compte le nombre de spectateurs par nationalité
     */
    @Query("SELECT s.nationality, COUNT(s) FROM Spectator s GROUP BY s.nationality")
    List<Object[]> countByNationality();

    /**
     * Trouve les spectateurs par catégorie comportementale
     */
    List<Spectator> findByCategory(String category);

    /**
     * Top N spectateurs les plus actifs
     */
    @Query("SELECT s FROM Spectator s ORDER BY s.totalMatches DESC")
    List<Spectator> findTopSpectatorsByMatchesAttended();

    /**
     * Compte total des spectateurs
     */
    @Query("SELECT COUNT(s) FROM Spectator s")
    Long countTotalSpectators();
}
