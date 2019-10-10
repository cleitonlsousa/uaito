package com.uaito.service;

import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Player;
import com.uaito.dto.Round;
import com.uaito.dto.TournamentDetails;
import com.uaito.enuns.MatchResultEnum;
import com.uaito.exception.NotFoundException;
import com.uaito.exception.RoundNotFinishException;
import com.uaito.exception.TournamentDetailsParseException;
import com.uaito.util.JsonUtil;
import com.uaito.validation.MatchValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchValidation matchValidation;

    @Autowired
    private JsonUtil jsonUtil;

    public void cancelRound(Long tournamentId, Long created, Integer round) throws TournamentDetailsParseException, NotFoundException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        tournament.getTournamentDetails().getRounds().removeIf( r -> r.getSequence().equals(round));

        tournamentService.update(tournament);
    }

    Round findRound(TournamentDetails details) {

        return details.getRounds().stream().filter(r -> !r.isComplete()).findAny().orElse(null);

    }

    public Round nextRound(Long tournamentId, Long created) throws TournamentDetailsParseException, NotFoundException, RoundNotFinishException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        if(CollectionUtils.isEmpty(tournament.getTournamentDetails().getRounds()))
            tournament.getTournamentDetails().setRounds(new ArrayList<>());

        // All matches must have a result filled in
        Round latestRound = getLatestRound(tournament.getTournamentDetails().getRounds());

        if (!(latestRound == null || latestRound.isComplete())) {

            throw new RoundNotFinishException();
        }

        matchValidation.isAllMatchValid(tournament);

        int roundNumber = tournament.getTournamentDetails().getRounds().size() + 1;

        List<Match> matches = (roundNumber == 1) ? firstRoundPairings(tournament.getTournamentDetails().getPlayers()) :
                roundPairings(tournament);

        Round round = new Round(roundNumber,matches);

        tournament.getTournamentDetails().getRounds().add(round);

        tournamentService.update(tournament);

        return round;

    }

    private List<Match> firstRoundPairings(List<Player> players) {

        List<Player> nonPairedPlayers = new ArrayList<>(players);

        List<Player> firstRoundByePlayers = nonPairedPlayers.stream()
                .filter(p -> p.isFirstRoundBye()).collect(Collectors.toList());

        nonPairedPlayers.removeAll(firstRoundByePlayers);

        List<Match> matches = initialSeedingRandom(nonPairedPlayers);

        for (Player p : firstRoundByePlayers) {
            matches.add(new Match( matches.size() + 1, p, null));
        }

        return matches;
    }

    private List<Match> initialSeedingRandom(List<Player> players) {

        List<Match> matches = new ArrayList<>();
        Collections.shuffle(players);

        int i = 1;
        while (!players.isEmpty()) {
            Player player1 = players.get(0);
            Player player2 = players.get(players.size() - 1);
            players.remove(player1);
            if (player1 == player2) {
                player2 = null;
            } else {
                players.remove(player2);
            }

            matches.add(new Match(i++, player1, player2));
        }
        return matches;
    }

    private List<Match> roundPairings(Tournament t) {

        List<Match> matches;

        List<Player> tempList = new ArrayList<>(t.getTournamentDetails().getPlayers());

        tempList.sort(tournamentService.getPairingComparator(t));

        int i = 1;

        Match byeMatch = null;
        // Setup the bye match if necessary
        // The player to get the bye is the lowest ranked player who has not had
        // a bye yet or who has the fewest byes
        if (tempList.size() % 2 == 1) {
            Player byeUser = null;
            int byUserCounter = 1;
            int minByes = 0;
            try {
                while (byeUser == null || validateBye(byeUser,t, minByes)) {

                    if (byUserCounter > tempList.size()) {
                        minByes++;
                        byUserCounter = 1;
                    }
                    byeUser = tempList.get(tempList.size() - byUserCounter);

                    byUserCounter++;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                byeUser = tempList.get(tempList.size() - 1);
            }

            byeMatch = new Match(i++, byeUser, null);

            tempList.remove(byeUser);

        }

        matches = matchService.generateMatches(t, tempList);

        // Add the bye match at the end
        if (byeMatch != null) {
            byeMatch.setMatchResultEnum(MatchResultEnum.PLAYER_1_WINS);
            matches.add(byeMatch);
        }

        matchService.validateMatches(matches, t.getPointsSize());

        return matches;

    }

    private boolean validateBye(Player byeUser, Tournament t, int minByes) {

        List<Match> playerMatches = playerService.getPlayerMatches(byeUser, t);

        int byes = playerService.getByes(t, byeUser);

        return (
                (byes > minByes)
                ||(playerMatches.get(playerMatches.size() -1).isBye())
        );

    }

    private Round getLatestRound(List<Round> rounds) {

        return CollectionUtils.isEmpty(rounds) ? null : rounds.get(rounds.size() -1);

    }

}
