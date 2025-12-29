package com.example.analyse_comportement_spectateurs.model.Dtos;
import com.example.analyse_comportement_spectateurs.batch.adapter.LocalDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Annotations JSON
// Annotations XML
@XmlRootElement(name = "spectatorEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpectatorEntryDto {

    @JsonProperty("spectatorId")
    @XmlElement(name = "spectatorId")
    private String spectatorId;

    @JsonProperty("matchId")
    @XmlElement(name = "matchId")
    private String matchId;

    @JsonProperty("entryTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @XmlElement(name = "entryTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime entryTime;

    @JsonProperty("gate")
    @XmlElement(name = "gate")
    private String gate;

    @JsonProperty("ticketNumber")
    @XmlElement(name = "ticketNumber")
    private String ticketNumber;

    @JsonProperty("age")
    @XmlElement(name = "age")
    private Integer age;

    @JsonProperty("nationality")
    @XmlElement(name = "nationality")
    private String nationality;

    @JsonProperty("ticketType")
    @XmlElement(name = "ticketType")
    private String ticketType;

    @JsonProperty("seatLocation")
    @XmlElement(name = "seatLocation")
    private SeatLocationDto seatLocation;
}