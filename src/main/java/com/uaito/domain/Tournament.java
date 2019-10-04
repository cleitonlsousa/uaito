package com.uaito.domain;

import com.uaito.dto.TournamentDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Tournament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long created;
    private String name;
    private String location;
    private Integer rounds;

    @Column(name="date")
    private LocalDateTime dateTime;

    @Column(name="points_size")
    private Integer pointsSize;

    @Column(name="default_round_length")
    private Integer defaultRoundLength;

    private Integer tickets;

    private String details;

    @Transient
    private TournamentDetails tournamentDetails;

    public Integer getByePoints(){
        return getPointsSize() + (getPointsSize() / 2);
    }

    public long getId() {
        return id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TournamentDetails getTournamentDetails() {
        return tournamentDetails;
    }

    public void setTournamentDetails(TournamentDetails tournamentDetails) {
        this.tournamentDetails = tournamentDetails;
    }
}
