package com.uaito.dto;

import com.uaito.enuns.FactionEnum;


public class Player implements Comparable<Player>{

    private Long id;
    private String firstName;
    private String lastName;
    private boolean firstRoundBye = false;
    private boolean isActive = true;
    private FactionEnum factionEnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isFirstRoundBye() {
        return firstRoundBye;
    }

    public void setFirstRoundBye(boolean firstRoundBye) {
        this.firstRoundBye = firstRoundBye;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public FactionEnum getFactionEnum() {
        return factionEnum;
    }

    public void setFactionEnum(FactionEnum factionEnum) {
        this.factionEnum = factionEnum;
    }

    @Override
    public int compareTo(Player player) {
        return this.getFirstName().toUpperCase().compareTo(player.getFirstName().toUpperCase());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId().equals(((Player)obj).getId());
    }
}
