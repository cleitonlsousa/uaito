package com.uaito.response;

import com.uaito.enuns.FactionEnum;

public class PlayerResponse {

    private Long account;
    private String firstName;
    private String lastName;
    private Boolean firstRoundBye;
    private FactionEnum factionEnum;
    private String squad;
    private Boolean active;

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
