package com.uaito.dto;

import com.uaito.enuns.MatchResultEnum;

import java.util.List;
import java.util.TreeSet;

public class Match {

    private Integer id;
    private Player player1;
    private Player player2;
    private MatchResultEnum matchResultEnum;
    private Integer player1Points;
    private Integer player2Points;
    private boolean isDuplicate;
    private boolean isConcede;

    public Match(Integer id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;

        initializer();
    }

    private void initializer() {
        this.matchResultEnum = null;
        this.player1Points = null;
        this.player2Points = null;
    }

    public boolean isComplete(){

        return (
                    (isBye() && player1Points != null)
                ||
                    (matchResultEnum != null)
                    && (player1Points != null)
                    &&(player2Points != null)
                );
    }

    public boolean checkDuplicate(List<Round> rounds) {

        if (this.getPlayer2() == null) {

            return Boolean.FALSE;

        }

        for (Round r : rounds) {

            TreeSet<Match> uniqueMatchSet = new TreeSet<>((o1, o2) -> {
                if (o1.getPlayer1() == o2.getPlayer1() && o1.getPlayer2() == o2.getPlayer2()) {
                    return 0;
                }

                return o1.getPlayer1().compareTo(o2.getPlayer1());
            });

            uniqueMatchSet.addAll(r.getMatches());

            for (Match match : uniqueMatchSet) {
                if (match.getPlayer2() == null || match == this) {
                    continue;
                }

                if ((match.getPlayer1() == this.getPlayer1() && match.getPlayer2() == this.getPlayer2())
                        || (match.getPlayer1() == this.getPlayer2() && match.getPlayer2() == this.getPlayer1())) {

                    return Boolean.TRUE;

                }
            }
        }

        return Boolean.FALSE;

    }

    public Player getWinner(){

        return MatchResultEnum.PLAYER_1_WINS.equals(getMatchResultEnum()) ? getPlayer1() :
                MatchResultEnum.PLAYER_2_WINS.equals(getMatchResultEnum()) ? getPlayer2() : null;

    }

    public boolean isWinner(Player player){

        return (
                    (getMatchResultEnum() != null)
                    &&(getWinner().equals(player))
        );

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isBye() {
        return player2 == null;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public MatchResultEnum getMatchResultEnum() {
        return matchResultEnum;
    }

    public void setMatchResultEnum(MatchResultEnum matchResultEnum) {
        this.matchResultEnum = matchResultEnum;
    }

    public Integer getPlayer1Points() {
        return player1Points;
    }

    public void setPlayer1Points(Integer player1Points) {
        this.player1Points = player1Points;
    }

    public Integer getPlayer2Points() {
        return player2Points;
    }

    public void setPlayer2Points(Integer player2Points) {
        this.player2Points = player2Points;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public boolean isConcede() {
        return isConcede;
    }

    public void setConcede(boolean concede) {
        isConcede = concede;
    }

    public static boolean hasDuplicate(List<Match> matches) {
        boolean duplicateFound = false;
        for (Match dc : matches) {
            if (dc.isDuplicate()) {
                duplicateFound = true;
                break;
            }
        }

        return duplicateFound;
    }

    @Override
    public String toString() {
        return getPlayer1().getFirstName() + (isBye() ? " Has a bye" : " vs " + getPlayer2().getFirstName());
    }

}
