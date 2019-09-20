package com.uaito.dto;

import com.uaito.domain.Tournament;
import com.uaito.exceptions.RoundException;
import com.uaito.Constants;
import com.uaito.util.RandomMatchGeneration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TournamentDetails implements Serializable {


    @Autowired
    private RandomMatchGeneration randomMatchGeneration;

    private List<Round> rounds;
    private List<Player> players;

    public void create(){

        this.rounds = new ArrayList<>();
        this.players = new ArrayList<>();

    }

    public void nextRound(Tournament tournament) throws RoundException{

        for (Round r : rounds) {
            if(!r.isComplete()){
                throw new RoundException(Constants.MATCH_NOT_FINISH);
            }
        }

        List<Match> matches = randomMatchGeneration.generateMatches(tournament, players);

        rounds.add(new Round(rounds.size() + 1 , matches));

    }

    public int playerScore(Player p){

        int score = 0;

        for (Round r : rounds) {

            for (Match m : r.getMatches()) {

                if (p.equals(m.getWinner())){
                    score += Constants.WIN_POINTS;

                }else if (m.isBye()){
                    score += Constants.BYE_POINTS;

                }else{
                    score += Constants.LOSS_POINTS;
                }

            }

        }

        return score;

    }
}
