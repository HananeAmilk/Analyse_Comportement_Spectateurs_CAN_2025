package com.example.analyse_comportement_spectateurs.batch.validator;


import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validateur pour les données des spectateurs
 * Applique les règles de validation métier
 */
@Component
public class SpectatorValidator {

    private static final Logger logger = LoggerFactory.getLogger(SpectatorValidator.class);

    // Patterns de validation
    private static final Pattern SPECTATOR_ID_PATTERN = Pattern.compile("^SPX\\d{5}$");
    private static final Pattern MATCH_ID_PATTERN = Pattern.compile("^MCH\\d{2,}$");
    private static final Pattern TICKET_PATTERN = Pattern.compile("^TK-\\d{5}-[A-Z]{3}$");
    private static final Pattern GATE_PATTERN = Pattern.compile("^Gate [A-Z]\\d{1,2}$");

    // Constantes métier
    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 120;
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(2025, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(2026, 12, 31, 23, 59);

    /**
     * Valide un spectateur et retourne la liste des erreurs
     * @param dto le DTO à valider
     * @return liste des messages d'erreur (vide si valide)
     */
    public List<String> validate(SpectatorEntryDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Le spectateur est null");
            return errors;
        }

        // Validation spectatorId
        if (!isValidSpectatorId(dto.getSpectatorId())) {
            errors.add("SpectatorId invalide: " + dto.getSpectatorId());
        }

        // Validation matchId
        if (!isValidMatchId(dto.getMatchId())) {
            errors.add("MatchId invalide: " + dto.getMatchId());
        }

        // Validation ticketNumber
        if (!isValidTicketNumber(dto.getTicketNumber())) {
            errors.add("Numéro de ticket invalide: " + dto.getTicketNumber());
        }

        // Validation gate
        if (!isValidGate(dto.getGate())) {
            errors.add("Porte invalide: " + dto.getGate());
        }

        // Validation age
        if (!isValidAge(dto.getAge())) {
            errors.add("Âge invalide: " + dto.getAge());
        }

        // Validation nationality
        if (!isValidNationality(dto.getNationality())) {
            errors.add("Nationalité invalide ou vide");
        }

        // Validation ticketType
        if (!isValidTicketType(dto.getTicketType())) {
            errors.add("Type de ticket invalide: " + dto.getTicketType());
        }

        // Validation entryTime
        if (!isValidEntryTime(dto.getEntryTime())) {
            errors.add("Date d'entrée invalide: " + dto.getEntryTime());
        }

        // Validation seatLocation
        if (!isValidSeatLocation(dto)) {
            errors.add("Localisation du siège invalide");
        }

        if (!errors.isEmpty()) {
            logger.warn("Validation échouée pour spectatorId {}: {}",
                    dto.getSpectatorId(), String.join(", ", errors));
        }

        return errors;
    }

    /**
     * Vérifie si un spectateur est valide
     */
    public boolean isValid(SpectatorEntryDto dto) {
        return validate(dto).isEmpty();
    }

    // ============= Méthodes de validation individuelles =============

    private boolean isValidSpectatorId(String spectatorId) {
        return spectatorId != null && SPECTATOR_ID_PATTERN.matcher(spectatorId).matches();
    }

    private boolean isValidMatchId(String matchId) {
        return matchId != null && MATCH_ID_PATTERN.matcher(matchId).matches();
    }

    private boolean isValidTicketNumber(String ticketNumber) {
        return ticketNumber != null && TICKET_PATTERN.matcher(ticketNumber).matches();
    }

    private boolean isValidGate(String gate) {
        return gate != null && GATE_PATTERN.matcher(gate).matches();
    }

    private boolean isValidAge(Integer age) {
        return age != null && age >= MIN_AGE && age <= MAX_AGE;
    }

    private boolean isValidNationality(String nationality) {
        return nationality != null &&
                !nationality.trim().isEmpty() &&
                nationality.length() >= 2 &&
                nationality.length() <= 100;
    }

    private boolean isValidTicketType(String ticketType) {
        if (ticketType == null || ticketType.trim().isEmpty()) {
            return false;
        }

        // Types de tickets valides
        List<String> validTypes = List.of("VIP", "Standard", "Premium", "Economique");
        return validTypes.contains(ticketType);
    }

    private boolean isValidEntryTime(LocalDateTime entryTime) {
        if (entryTime == null) {
            return false;
        }

        return !entryTime.isBefore(MIN_DATE) && !entryTime.isAfter(MAX_DATE);
    }

    private boolean isValidSeatLocation(SpectatorEntryDto dto) {
        if (dto.getSeatLocation() == null) {
            return false;
        }

        var location = dto.getSeatLocation();

        // Validation tribune
        if (location.getTribune() == null || location.getTribune().trim().isEmpty()) {
            return false;
        }

        // Validation bloc
        if (location.getBloc() == null || location.getBloc().trim().isEmpty()) {
            return false;
        }

        // Validation rang (doit être positif)
        if (location.getRang() == null || location.getRang() <= 0) {
            return false;
        }

        // Validation siège (doit être positif)
        if (location.getSiege() == null || location.getSiege() <= 0) {
            return false;
        }

        return true;
    }
}
