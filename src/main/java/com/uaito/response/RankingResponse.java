package com.uaito.response;

public class RankingResponse {

    private String name;
    private Integer score;
    private Integer mov;
    private Double sos;
    private Integer byes;
    private Integer wins;
    private Integer losses;

    public String getRecord() {
        return getWins() + " / " + getLosses();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMov() {
        return mov;
    }

    public void setMov(Integer mov) {
        this.mov = mov;
    }

    public Double getSos() {
        return sos;
    }

    public void setSos(Double sos) {
        this.sos = sos;
    }

    public Integer getByes() {
        return byes;
    }

    public void setByes(Integer byes) {
        this.byes = byes;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }
}
