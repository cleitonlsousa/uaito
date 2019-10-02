package com.uaito.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TournamentDetails implements Serializable {

    private List<Round> rounds;
    private List<Player> players;

    public TournamentDetails() {
        this.rounds = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
