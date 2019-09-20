package com.uaito.dto;

import com.uaito.enuns.MatchResult;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Getter
@Setter
public class Match {

    private Player player1;
    private Player player2;
    private MatchResult matchResult;
    private Integer player1Points;
    private Integer player2Points;
    private boolean isDuplicate;
    private boolean isConcede;

    public Match() {
        this.matchResult = null;
        this.player1Points = null;
        this.player2Points = null;
    }

    public boolean isComplete(){

        return (
                    (matchResult != null)
                    && (player1Points != null)
                    &&(player2Points != null)
                );
    }

    public boolean checkDuplicate(List<Round> rounds) {

        if (this.getPlayer2() == null) {

            return Boolean.FALSE;

        }

        for (Round r : rounds) {

            TreeSet<Match> uniqueMatchSet = new TreeSet<Match>(new Comparator<Match>() {

                @Override
                public int compare(Match o1, Match o2) {
                    if(o1.getPlayer1() == o2.getPlayer1() && o1.getPlayer2() == o2.getPlayer2()){
                        return 0;
                    }

                    return o1.getPlayer1().compareTo(o2.getPlayer1());
                }
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

        return MatchResult.PLAYER_1_WINS.equals(getMatchResult()) ? getPlayer1() :
                MatchResult.PLAYER_2_WINS.equals(getMatchResult()) ? getPlayer2() : null;

    }

    public boolean isBye() {
        return player2 == null;
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

}
