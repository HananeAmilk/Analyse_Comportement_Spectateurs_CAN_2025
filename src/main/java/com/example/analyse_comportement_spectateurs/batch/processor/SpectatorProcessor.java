package com.example.analyse_comportement_spectateurs.batch.processor;

import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import com.example.analyse_comportement_spectateurs.model.Entities.Entry;
import com.example.analyse_comportement_spectateurs.model.Entities.Spectator;
import com.example.analyse_comportement_spectateurs.batch.validator.SpectatorValidator;
import com.example.analyse_comportement_spectateurs.repositories.EntryRepository;
import com.example.analyse_comportement_spectateurs.repositories.SpectatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpectatorProcessor implements ItemProcessor<SpectatorEntryDto, Map<String, Object>> {

    @Autowired
    private SpectatorValidator validator;

    @Autowired
    private SpectatorRepository spectatorRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Override
    public Map<String, Object> process(SpectatorEntryDto dto) throws Exception {
        // 1. Validation
        List<String> errors = validator.validate(dto);
        if (!errors.isEmpty()) {
            return null; // Item sera skippé
        }

        // 2. Vérification du ticket dupliqué
        if (entryRepository.existsByTicketNumber(dto.getTicketNumber())) {
            return null; // Skip les doublons
        }

        // 3. Création ou mise à jour du Spectator
        Spectator spectator = createOrUpdateSpectator(dto);

        // 4. Création de l'Entry
        Entry entry = createEntry(dto);

        // 5. Retour des données à persister
        Map<String, Object> result = new HashMap<>();
        result.put("spectator", spectator);
        result.put("entry", entry);

        return result;
    }

    /**
     * Crée ou met à jour un spectateur
     */
    private Spectator createOrUpdateSpectator(SpectatorEntryDto dto) {
        // Recherche si le spectateur existe déjà
        Spectator spectator = spectatorRepository
                .findBySpectatorId(dto.getSpectatorId())
                .orElse(null);

        if (spectator == null) {
            // Nouveau spectateur
            spectator = new Spectator();
            spectator.setSpectatorId(dto.getSpectatorId());
            spectator.setAge(dto.getAge());
            spectator.setNationality(dto.getNationality());
            spectator.setTotalMatches(1);
        } else {
            // Spectateur existant - mise à jour
            spectator.setTotalMatches(spectator.getTotalMatches() + 1);
        }

        // Classification comportementale
        spectator.setCategory(
                calculateBehavioralCategory(spectator.getTotalMatches())
        );

        return spectator;
    }

    /**
     * Crée une entrée (participation à un match)
     */
    private Entry createEntry(SpectatorEntryDto dto) {
        Entry entry = new Entry();
        entry.setSpectatorId(dto.getSpectatorId());
        entry.setMatchId(dto.getMatchId());
        entry.setEntryTime(dto.getEntryTime());
        entry.setGate(dto.getGate());
        entry.setTicketNumber(dto.getTicketNumber());
        entry.setTicketType(dto.getTicketType());

        // Décomposition de la localisation du siège
        if (dto.getSeatLocation() != null) {
            entry.setTribune(dto.getSeatLocation().getTribune());
            entry.setBloc(dto.getSeatLocation().getBloc());
            entry.setRang(dto.getSeatLocation().getRang());
            entry.setSiege(dto.getSeatLocation().getSiege());
        }

        return entry;
    }

    /**
     * Calcule la classification comportementale selon le nombre de matchs
     */
    private String calculateBehavioralCategory(int matchesAttended) {
        if (matchesAttended == 1) {
            return "Première visite";
        } else if (matchesAttended >= 2 && matchesAttended <= 3) {
            return "Spectateur occasionnel";
        } else if (matchesAttended >= 4 && matchesAttended <= 6) {
            return "Spectateur régulier";
        } else {
            return "Super fan";
        }
    }
}