package com.uaito.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Round {

    private Integer sequence;
    private List<Match> matches;

    public Round(Integer sequence, List<Match> matches) {
        this.sequence = sequence;
        this.matches = matches;
    }

    public Boolean isComplete() {
        for (Match m : getMatches()) {
            if (!m.isComplete()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

}
