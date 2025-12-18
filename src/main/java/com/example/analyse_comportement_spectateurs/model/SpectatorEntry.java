package com.example.analyse_comportement_spectateurs.model;

// Input DTO
public class SpectatorEntry {
    private String spectatorId;
    private String matchId;
    private String entryTime;
    private String gate;
    private String ticketNumber;
    private Integer age;
    private String nationality;
    private String ticketType;
    private SeatLocation seatLocation;

    // Getters and Setters
    public String getSpectatorId() { return spectatorId; }
    public void setSpectatorId(String spectatorId) {
        this.spectatorId = spectatorId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public SeatLocation getSeatLocation() {
        return seatLocation;
    }

    public void setSeatLocation(SeatLocation seatLocation) {
        this.seatLocation = seatLocation;
    }
}
