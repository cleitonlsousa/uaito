package com.uaito.request;

public class MatchResultRequest {

    private Integer match;
    private Long player1;
    private Integer player1Result;
    private Long player2;
    private Integer player2Result;

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public Long getPlayer1() {
        return player1;
    }

    public void setPlayer1(Long player1) {
        this.player1 = player1;
    }

    public Integer getPlayer1Result() {
        return player1Result;
    }

    public void setPlayer1Result(Integer player1Result) {
        this.player1Result = player1Result;
    }

    public Long getPlayer2() {
        return player2;
    }

    public void setPlayer2(Long player2) {
        this.player2 = player2;
    }

    public Integer getPlayer2Result() {
        return player2Result;
    }

    public void setPlayer2Result(Integer player2Result) {
        this.player2Result = player2Result;
    }
}
