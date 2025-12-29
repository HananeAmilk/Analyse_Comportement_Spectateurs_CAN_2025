package com.example.analyse_comportement_spectateurs.model.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SeatLocationDto {

    @JsonProperty("tribune")
    @XmlElement(name = "tribune")
    private String tribune;

    @JsonProperty("bloc")
    @XmlElement(name = "bloc")
    private String bloc;

    @JsonProperty("rang")
    @XmlElement(name = "rang")
    private Integer rang;

    @JsonProperty("siege")
    @XmlElement(name = "siege")
    private Integer siege;
}
