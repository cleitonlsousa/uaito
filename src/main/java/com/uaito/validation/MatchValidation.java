package com.uaito.validation;

import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.exception.ConsumerWrapper;
import com.uaito.exception.MatchResultException;
import org.springframework.stereotype.Component;

@Component
public class MatchValidation {

    public void isAllMatchValid(Tournament tournament) {

        tournament.getTournamentDetails().getRounds()
                .forEach( r -> r.getMatches()
                        .forEach(ConsumerWrapper.wrap(m -> isValidResult(m, tournament.getPointsSize(), tournament.getByePoints()))));

    }

    private void isValidResult(Match m, int tournamentPoints, int byePoints) throws MatchResultException {

        Integer player1Points = m.getPlayer1Points() == null ? 0 : m.getPlayer1Points();
        Integer player2Points = m.getPlayer2Points() == null ? 0 : m.getPlayer2Points();

        if(m.isBye() && player1Points > byePoints)
            throw new MatchResultException(m.toString());

        if(!m.isBye() && player1Points > tournamentPoints)
            throw new MatchResultException(m.toString());

        if(player2Points > tournamentPoints)
            throw new MatchResultException(m.toString());

        // If there is no second player, it must be a bye
        if (m.getPlayer2() == null && !m.isBye())
            throw new MatchResultException(m.toString());

    }

}
