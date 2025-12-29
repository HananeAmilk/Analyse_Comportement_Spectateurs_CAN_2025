package com.example.analyse_comportement_spectateurs.service;

import com.example.analyse_comportement_spectateurs.model.Entities.Spectator;
import com.example.analyse_comportement_spectateurs.model.Entities.Statistic;
import com.example.analyse_comportement_spectateurs.repositories.EntryRepository;
import com.example.analyse_comportement_spectateurs.repositories.SpectatorRepository;
import com.example.analyse_comportement_spectateurs.repositories.StatisticRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour calculer et persister les statistiques dérivées
 */
@Service
public class StatisticService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticService.class);

    @Autowired
    private SpectatorRepository spectatorRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Calcule toutes les statistiques dérivées
     */
    @Transactional
    public void calculateAllStatistics() {
        logger.info("Début du calcul des statistiques dérivées");

        try {
            calculateTotalSpectators();
            calculateNationalityDistribution();
            calculateTicketTypeDistribution();
            calculateGateOccupancy();
            calculateAverageArrivalTime();
            calculateBehavioralCategoryDistribution();
            calculateTopActiveSpectators();
            calculateTribuneOccupancy();
            calculateMatchAttendance();
            calculateMinMaxArrivalTimes();
            calculateLoyaltyRate();

            logger.info("Calcul des statistiques terminé avec succès");

        } catch (Exception e) {
            logger.error("Erreur lors du calcul des statistiques: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 1. Nombre total de spectateurs
     */
    private void calculateTotalSpectators() {
        Long total = spectatorRepository.countTotalSpectators();
        saveStatistic("total_spectators", total.toString(), "COUNT",
                "Nombre total de spectateurs uniques");
    }

    /**
     * 2. Répartition des spectateurs par nationalité
     */
    private void calculateNationalityDistribution() {
        List<Object[]> results = spectatorRepository.countByNationality();
        Map<String, Long> distribution = new HashMap<>();

        for (Object[] result : results) {
            String nationality = (String) result[0];
            Long count = (Long) result[1];
            distribution.put(nationality, count);
        }

        saveStatistic("nationality_distribution", toJson(distribution),
                "DISTRIBUTION", "Répartition par nationalité");
    }

    /**
     * 3. Répartition par type de ticket
     */
    private void calculateTicketTypeDistribution() {
        List<Object[]> results = entryRepository.countByTicketType();
        Map<String, Long> distribution = new HashMap<>();

        for (Object[] result : results) {
            String ticketType = (String) result[0];
            Long count = (Long) result[1];
            distribution.put(ticketType, count);
        }

        saveStatistic("ticket_type_distribution", toJson(distribution),
                "DISTRIBUTION", "Répartition par type de ticket");
    }

    /**
     * 4. Taux d'occupation des portes d'accès
     */
    private void calculateGateOccupancy() {
        List<Object[]> results = entryRepository.countByGate();
        Map<String, Long> occupancy = new HashMap<>();

        for (Object[] result : results) {
            String gate = (String) result[0];
            Long count = (Long) result[1];
            occupancy.put(gate, count);
        }

        saveStatistic("gate_occupancy", toJson(occupancy),
                "OCCUPANCY", "Taux d'occupation par porte");
    }

    /**
     * 5. Temps moyen d'arrivée
     */
    private void calculateAverageArrivalTime() {
        Double avgHour = entryRepository.getAverageEntryHour();

        if (avgHour != null) {
            String formattedTime = String.format("%.2f heures", avgHour);
            saveStatistic("average_arrival_time", formattedTime,
                    "AVERAGE", "Heure moyenne d'arrivée");
        }
    }

    /**
     * 6. Répartition par catégorie comportementale
     */
    private void calculateBehavioralCategoryDistribution() {
        List<String> categories = Arrays.asList(
                "Première visite",
                "Spectateur occasionnel",
                "Spectateur régulier",
                "Super fan"
        );

        Map<String, Long> distribution = new HashMap<>();

        for (String category : categories) {
            long count = spectatorRepository.findByCategory(category).size();
            distribution.put(category, count);
        }

        saveStatistic("behavioral_category_distribution", toJson(distribution),
                "DISTRIBUTION", "Répartition par catégorie comportementale");
    }

    /**
     * 7. Top 10 spectateurs les plus actifs
     */
    private void calculateTopActiveSpectators() {
        List<Spectator> topSpectators = spectatorRepository
                .findTopSpectatorsByMatchesAttended();

        List<Map<String, Object>> top10 = topSpectators.stream()
                .limit(10)
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("spectatorId", s.getSpectatorId());
                    map.put("matchesAttended", s.getTotalMatches());
                    map.put("category", s.getCategory());
                    return map;
                })
                .collect(Collectors.toList());

        saveStatistic("top_10_active_spectators", toJson(top10),
                "RANKING", "Top 10 des spectateurs les plus actifs");
    }

    /**
     * 8. Affluence par tribune
     */
    private void calculateTribuneOccupancy() {
        List<Object[]> results = entryRepository.countByTribune();
        Map<String, Long> occupancy = new HashMap<>();

        for (Object[] result : results) {
            String tribune = (String) result[0];
            Long count = (Long) result[1];
            occupancy.put(tribune, count);
        }

        saveStatistic("tribune_occupancy", toJson(occupancy),
                "OCCUPANCY", "Affluence par tribune");
    }

    /**
     * 9. Évolution de la fréquentation match par match
     */
    private void calculateMatchAttendance() {
        List<Object[]> results = entryRepository.countEntriesByMatch();
        Map<String, Long> attendance = new LinkedHashMap<>();

        for (Object[] result : results) {
            String matchId = (String) result[0];
            Long count = (Long) result[1];
            attendance.put(matchId, count);
        }

        saveStatistic("match_attendance", toJson(attendance),
                "EVOLUTION", "Évolution de la fréquentation par match");
    }

    /**
     * 10. Temps minimum et maximum d'arrivée
     */
    private void calculateMinMaxArrivalTimes() {
        List<Object[]> results = entryRepository.getMinMaxEntryTimes();

        if (!results.isEmpty() && results.get(0)[0] != null) {
            LocalDateTime minTime = (LocalDateTime) results.get(0)[0];
            LocalDateTime maxTime = (LocalDateTime) results.get(0)[1];

            Map<String, String> times = new HashMap<>();
            times.put("earliest", minTime.toString());
            times.put("latest", maxTime.toString());

            saveStatistic("min_max_arrival_times", toJson(times),
                    "RANGE", "Heures d'arrivée minimum et maximum");
        }
    }

    /**
     * 11. Taux de fidélité (matchs assistés / total matchs)
     */
    private void calculateLoyaltyRate() {
        // Nombre total de matchs (fictif pour l'exemple)
        long totalMatches = entryRepository.countEntriesByMatch().size();

        if (totalMatches > 0) {
            List<Spectator> spectators = spectatorRepository.findAll();

            double avgMatchesPerSpectator = spectators.stream()
                    .mapToInt(Spectator::getTotalMatches)
                    .average()
                    .orElse(0.0);

            double loyaltyRate = (avgMatchesPerSpectator / totalMatches) * 100;

            saveStatistic("loyalty_rate",
                    String.format("%.2f%%", loyaltyRate),
                    "PERCENTAGE",
                    "Taux de fidélité moyen");
        }
    }

    /**
     * Sauvegarde une statistique
     */
    private void saveStatistic(String name, String value, String type, String description) {
        try {
            Statistic stat = statisticRepository.findByStatName(name)
                    .orElse(new Statistic());

            stat.setStatName(name);
            stat.setStatValue(value);
            stat.setStatType(type);
            stat.setDescription(description);

            statisticRepository.save(stat);
            logger.debug("Statistique sauvegardée: {}", name);

        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde de {}: {}", name, e.getMessage(), e);
            throw e; // stoppe tout
        }

    }

    /**
     * Convertit un objet en JSON
     */
    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Erreur de conversion JSON: {}", e.getMessage());
            return "{}";
        }
    }
}