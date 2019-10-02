package com.uaito.response;

import com.uaito.dto.TournamentDetails;

import java.time.LocalDateTime;

public class TournamentResponse {

    private Long id;
    private AccountResponse created;
    private String name;
    private String location;
    private Integer rounds;
    private LocalDateTime dateTime;
    private Integer pointsSize;
    private Integer defaultRoundLength;
    private Integer tickets;
    private TournamentDetails details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountResponse getCreated() {
        return created;
    }

    public void setCreated(AccountResponse created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getPointsSize() {
        return pointsSize;
    }

    public void setPointsSize(Integer pointsSize) {
        this.pointsSize = pointsSize;
    }

    public Integer getDefaultRoundLength() {
        return defaultRoundLength;
    }

    public void setDefaultRoundLength(Integer defaultRoundLength) {
        this.defaultRoundLength = defaultRoundLength;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public TournamentDetails getDetails() {
        return details;
    }

    public void setDetails(TournamentDetails details) {
        this.details = details;
    }
}
