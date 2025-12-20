package com.example.analyse_comportement_spectateurs.repositories;

import com.example.analyse_comportement_spectateurs.model.Entities.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    /**
     * Recherche une statistique par son nom
     */
    Optional<Statistic> findByStatName(String statName);

    /**
     * Trouve toutes les statistiques d'un type donné
     */
    List<Statistic> findByStatType(String statType);

    /**
     * Supprime une statistique par son nom
     */
    void deleteByStatName(String statName);

    /**
     * Vérifie si une statistique existe
     */
    boolean existsByStatName(String statName);
}