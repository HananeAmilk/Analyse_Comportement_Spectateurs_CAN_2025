package com.example.analyse_comportement_spectateurs.model.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryDto {
    private String spectatorId;
    private String matchId;
    private String entryTime;
    private String gate;
    private String ticketNumber;
    private Integer age;
    private String nationality;
    private String ticketType;
    private SeatLocationDto seatLocation;

}
