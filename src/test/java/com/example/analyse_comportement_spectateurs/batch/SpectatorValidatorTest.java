package com.example.analyse_comportement_spectateurs.batch;

import com.example.analyse_comportement_spectateurs.batch.validator.SpectatorValidator;
import com.example.analyse_comportement_spectateurs.model.Dtos.SeatLocationDto;
import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SpectatorValidatorTest {

    private SpectatorValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SpectatorValidator();
    }

    @Test
    void testValidSpectator() {
        SpectatorEntryDto dto = createValidDto();
        List<String> errors = validator.validate(dto);
        assertTrue(errors.isEmpty(), "Le spectateur valide ne devrait avoir aucune erreur");
    }

    @Test
    void testInvalidSpectatorId() {
        SpectatorEntryDto dto = createValidDto();
        dto.setSpectatorId("INVALID");
        List<String> errors = validator.validate(dto);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("SpectatorId invalide")));
    }

    @Test
    void testInvalidAge() {
        SpectatorEntryDto dto = createValidDto();
        dto.setAge(150);
        List<String> errors = validator.validate(dto);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("Âge invalide")));
    }

    @Test
    void testInvalidTicketNumber() {
        SpectatorEntryDto dto = createValidDto();
        dto.setTicketNumber("INVALID-TICKET");
        List<String> errors = validator.validate(dto);
        assertFalse(errors.isEmpty());
    }

    @Test
    void testNullSpectator() {
        List<String> errors = validator.validate(null);
        assertFalse(errors.isEmpty());
        assertEquals("Le spectateur est null", errors.get(0));
    }

    private SpectatorEntryDto createValidDto() {
        SeatLocationDto seatLocation = new SeatLocationDto("Nord", "A", 10, 5);

        return new SpectatorEntryDto(
                "SPX20001",
                "MCH01",
                LocalDateTime.of(2025, 7, 5, 17, 30),
                "Gate A1",
                "TK-10001-AGD",
                28,
                "Maroc",
                "Standard",
                seatLocation
        );
    }
}