package com.uaito.service;

import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Round;
import com.uaito.dto.TournamentDetails;
import com.uaito.exception.MatchNotFinishException;
import com.uaito.exception.NotFoundException;
import com.uaito.exception.TournamentDetailsParseException;
import com.uaito.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoundService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private JsonUtil jsonUtil;

    public Round nextRound(Long tournamentId, Long created) throws MatchNotFinishException, TournamentDetailsParseException, NotFoundException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        TournamentDetails details = (TournamentDetails) jsonUtil.parse(tournament.getDetails(), TournamentDetails.class);

        if(CollectionUtils.isEmpty(details.getRounds()))
            details.setRounds(new ArrayList<>());

        for (Round r : details.getRounds()) {
            if(!r.isComplete()){
                throw new MatchNotFinishException();
            }
        }

        List<Match> matches = matchService.generateMatches(tournament, details);

        Round round = new Round(details.getRounds().size() + 1 , matches);

        details.getRounds().add(round);

        tournament.setDetails(jsonUtil.convert(details));

        tournamentService.update(tournament);

        return round;

    }

    public void cancelRound(Long tournamentId, Long created, Integer round) throws TournamentDetailsParseException, NotFoundException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        TournamentDetails details = (TournamentDetails) jsonUtil.parse(tournament.getDetails(), TournamentDetails.class);

        details.getRounds().removeIf( r -> r.getSequence() == round);

        tournament.setDetails(jsonUtil.convert(details));

        tournamentService.update(tournament);
    }

}
