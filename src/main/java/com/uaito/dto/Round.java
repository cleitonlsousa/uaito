package com.uaito.dto;

import java.util.List;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
