package com.example.analyse_comportement_spectateurs.model.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatLocationDto {
    private String tribune;
    private String bloc;
    private Integer rang;
    private Integer siege;
}
