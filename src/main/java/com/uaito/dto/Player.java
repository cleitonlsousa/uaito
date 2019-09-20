package com.uaito.dto;

import com.uaito.domain.Account;
import com.uaito.enuns.Faction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player implements Comparable<Player>{

    private Account account;
    private String name;
    private String groupName;
    private String saveId;
    private String email;
    private String penaltyPoints;
    private Integer staticTable;
    private boolean firstRoundBye = false;
    private boolean isActive = true;
    private String squadId;
    private Faction faction;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Player player) {
        return this.getName().toUpperCase().compareTo(player.getName().toUpperCase());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getAccount().getId() == ((Player)obj).getAccount().getId();
    }
}
