package com.uaito.request;

import com.uaito.enuns.FactionEnum;

public class PlayerRequest {

    private Long id;
    private Boolean firstRoundBye;
    private FactionEnum factionEnum;
    private String squad;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFirstRoundBye() {
        return firstRoundBye;
    }

    public void setFirstRoundBye(Boolean firstRoundBye) {
        this.firstRoundBye = firstRoundBye;
    }

    public FactionEnum getFactionEnum() {
        return factionEnum;
    }

    public void setFactionEnum(FactionEnum factionEnum) {
        this.factionEnum = factionEnum;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
