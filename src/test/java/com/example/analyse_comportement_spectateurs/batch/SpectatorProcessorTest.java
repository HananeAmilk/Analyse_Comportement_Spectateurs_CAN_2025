package com.example.analyse_comportement_spectateurs.batch;

import com.example.analyse_comportement_spectateurs.batch.processor.SpectatorProcessor;
import com.example.analyse_comportement_spectateurs.batch.validator.SpectatorValidator;
import com.example.analyse_comportement_spectateurs.model.Dtos.SeatLocationDto;
import com.example.analyse_comportement_spectateurs.model.Dtos.SpectatorEntryDto;
import com.example.analyse_comportement_spectateurs.model.Entities.Entry;
import com.example.analyse_comportement_spectateurs.model.Entities.Spectator;
import com.example.analyse_comportement_spectateurs.repositories.EntryRepository;
import com.example.analyse_comportement_spectateurs.repositories.SpectatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpectatorProcessorTest {

    @Mock
    private SpectatorValidator validator;

    @Mock
    private SpectatorRepository spectatorRepository;

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private SpectatorProcessor processor;

    private SpectatorEntryDto validDto;

    @BeforeEach
    void setUp() {

        SeatLocationDto seatLocation = new SeatLocationDto("Est", "C", 4, 12);

//        validDto = new SpectatorEntryDto(
//                "SPX20245",
//                "MCH12",
//                "2025-07-05T17:42:10",  //String
//                "Gate A3",
//                "TK-55231-AGD",
//                34,
//                "Maroc",
//                "VIP",
//                seatLocation
//        );

        validDto = new SpectatorEntryDto(
                "SPX20245",
                "MCH12",
                LocalDateTime.of(2025, 7, 5, 17, 42, 10),  // ✅ LocalDateTime
                "Gate A3",
                "TK-55231-AGD",
                34,
                "Maroc",
                "VIP",
                seatLocation
        );
    }

    @Test
    void testProcess_NouveauSpectateur_Success() throws Exception {
        // Given
        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId())).thenReturn(Optional.empty());

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("spectator", "entry");

        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getSpectatorId()).isEqualTo("SPX20245");
        assertThat(spectator.getAge()).isEqualTo(34);
        assertThat(spectator.getNationality()).isEqualTo("Maroc");
        assertThat(spectator.getTotalMatches()).isEqualTo(1);
        assertThat(spectator.getCategory()).isEqualTo("Première visite");

        Entry entry = (Entry) result.get("entry");
        assertThat(entry.getSpectatorId()).isEqualTo("SPX20245");
        assertThat(entry.getMatchId()).isEqualTo("MCH12");
        assertThat(entry.getGate()).isEqualTo("Gate A3");
        assertThat(entry.getTicketNumber()).isEqualTo("TK-55231-AGD");
        assertThat(entry.getTicketType()).isEqualTo("VIP");
        assertThat(entry.getTribune()).isEqualTo("Est");
        assertThat(entry.getBloc()).isEqualTo("C");
        assertThat(entry.getRang()).isEqualTo(4);
        assertThat(entry.getSiege()).isEqualTo(12);

        verify(validator).validate(validDto);
        verify(entryRepository).existsByTicketNumber(validDto.getTicketNumber());
        verify(spectatorRepository).findBySpectatorId(validDto.getSpectatorId());
    }

    @Test
    void testProcess_SpectateurExistant_IncrementeTotalMatches() throws Exception {
        // Given
        Spectator existingSpectator = new Spectator();
        existingSpectator.setSpectatorId("SPX20245");
        existingSpectator.setAge(34);
        existingSpectator.setNationality("Maroc");
        existingSpectator.setTotalMatches(2);
        existingSpectator.setCategory("Spectateur occasionnel");

        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId()))
                .thenReturn(Optional.of(existingSpectator));

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        assertThat(result).isNotNull();

        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getTotalMatches()).isEqualTo(3);
        assertThat(spectator.getCategory()).isEqualTo("Spectateur occasionnel");
    }

    @Test
    void testProcess_ValidationEchouee_RetourneNull() throws Exception {
        // Given
        List<String> errors = Arrays.asList("Age invalide", "Nationalité manquante");
        when(validator.validate(validDto)).thenReturn(errors);

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        assertThat(result).isNull();
        verify(validator).validate(validDto);
        verify(entryRepository, never()).existsByTicketNumber(any());
        verify(spectatorRepository, never()).findBySpectatorId(any());
    }

    @Test
    void testProcess_TicketDuplique_RetourneNull() throws Exception {
        // Given
        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(true);

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        assertThat(result).isNull();
        verify(validator).validate(validDto);
        verify(entryRepository).existsByTicketNumber(validDto.getTicketNumber());
        verify(spectatorRepository, never()).findBySpectatorId(any());
    }

    @Test
    void testProcess_SansLocalisation_Success() throws Exception {
        // Given
        validDto.setSeatLocation(null);

        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId())).thenReturn(Optional.empty());

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        assertThat(result).isNotNull();

        Entry entry = (Entry) result.get("entry");
        assertThat(entry.getTribune()).isNull();
        assertThat(entry.getBloc()).isNull();
        assertThat(entry.getRang()).isNull();
        assertThat(entry.getSiege()).isNull();
    }

    @Test
    void testCalculateBehavioralCategory_PremiereVisite() throws Exception {
        // Given
        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId())).thenReturn(Optional.empty());

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getTotalMatches()).isEqualTo(1);
        assertThat(spectator.getCategory()).isEqualTo("Première visite");
    }

    @Test
    void testCalculateBehavioralCategory_SpectateurOccasionnel() throws Exception {
        // Given
        Spectator existingSpectator = new Spectator();
        existingSpectator.setSpectatorId("SPX20245");
        existingSpectator.setTotalMatches(1);

        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId()))
                .thenReturn(Optional.of(existingSpectator));

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getTotalMatches()).isEqualTo(2);
        assertThat(spectator.getCategory()).isEqualTo("Spectateur occasionnel");
    }

    @Test
    void testCalculateBehavioralCategory_SpectateurRegulier() throws Exception {
        // Given
        Spectator existingSpectator = new Spectator();
        existingSpectator.setSpectatorId("SPX20245");
        existingSpectator.setTotalMatches(3);

        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId()))
                .thenReturn(Optional.of(existingSpectator));

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getTotalMatches()).isEqualTo(4);
        assertThat(spectator.getCategory()).isEqualTo("Spectateur régulier");
    }

    @Test
    void testCalculateBehavioralCategory_SuperFan() throws Exception {
        // Given
        Spectator existingSpectator = new Spectator();
        existingSpectator.setSpectatorId("SPX20245");
        existingSpectator.setTotalMatches(6);

        when(validator.validate(validDto)).thenReturn(Collections.emptyList());
        when(entryRepository.existsByTicketNumber(validDto.getTicketNumber())).thenReturn(false);
        when(spectatorRepository.findBySpectatorId(validDto.getSpectatorId()))
                .thenReturn(Optional.of(existingSpectator));

        // When
        Map<String, Object> result = processor.process(validDto);

        // Then
        Spectator spectator = (Spectator) result.get("spectator");
        assertThat(spectator.getTotalMatches()).isEqualTo(7);
        assertThat(spectator.getCategory()).isEqualTo("Super fan");
    }
}