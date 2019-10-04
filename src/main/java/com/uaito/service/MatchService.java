package com.uaito.service;

import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Player;
import com.uaito.dto.Round;
import com.uaito.dto.TournamentDetails;
import com.uaito.enuns.MatchResultEnum;
import com.uaito.exception.ConsumerWrapper;
import com.uaito.exception.MatchResultException;
import com.uaito.exception.NotFoundException;
import com.uaito.exception.TournamentDetailsParseException;
import com.uaito.request.MatchResultRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class MatchService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoundService roundService;

    private List<List<Player>> pointGroups;
    private List<Player> players;

    List<Match> generateMatches(Tournament tournament, List<Player> players) {

        this.players = players;

        if(CollectionUtils.isEmpty(players))
            return new ArrayList<>();

        getPointGroups(tournament);

        List<Match> matches = null;

        if(!pointGroups.isEmpty())
            matches = resolvePointGroup(null, 0, tournament);

        return matches;
    }

    void validateMatches(List<Match> matches, Integer points){

        matches.forEach(match -> {
            if(match.isBye()) match.setPlayer1Points(points + (points/2));
        });

    }

    private void getPointGroups(Tournament tournament){

        TreeMap<Integer, List<Player>> playerMap = new TreeMap<>((arg0, arg1) -> arg0.compareTo(arg1) * -1);

        for (Player p : players) {

            Integer points = playerService.playerScore(p, tournament);

            List<Player> pointGroup = playerMap.computeIfAbsent(points, k -> new ArrayList<>());

            pointGroup.add(p);

        }

        pointGroups = new ArrayList<>();

        for(Integer i : playerMap.keySet()){
            pointGroups.add(playerMap.get(i));
        }
    }

    private List<Match> resolvePointGroup(Player carryOverPlayer, int pointGroupCounter, Tournament tournament) {

        if(pointGroupCounter >= pointGroups.size()){
            return new ArrayList<>();
        }

        // Get the point group players and mix them up
        List<Player> playerList = pointGroups.get(pointGroupCounter);
        Collections.shuffle(playerList);

        // Get the player to carry over to the next group
        Player newCarryOverPlayer = null;
        int carryOverPlayerIndex = playerList.size();
        boolean isCarryOver = carryOverPlayer == null ? carryOverPlayerIndex % 2 == 1
                : carryOverPlayerIndex % 2 == 0;

        while (true) {

            List<Player> tempList = new ArrayList<>(playerList);

            if (isCarryOver) {
                carryOverPlayerIndex--;
                newCarryOverPlayer = playerList.get(carryOverPlayerIndex);
                tempList.remove(newCarryOverPlayer);
            }

            List<Match> returnedMatches = getRandomMatches(carryOverPlayer, tempList, tournament.getTournamentDetails());

            // If the list was good or if there was no carry over players that
            // can change things up
            if (!isCarryOver || carryOverPlayerIndex == 0 || !Match.hasDuplicate(returnedMatches)) {

                List<Player> nextPointGroup = null;

                if(pointGroupCounter + 1 < pointGroups.size()){

                    nextPointGroup = pointGroups.get(pointGroupCounter + 1);

                }

                // If this was the last point group
                if (nextPointGroup == null) {

                    return returnedMatches;

                } else {

                    // Else, check the next point group
                    List<Match> nextPointGroupMatches = resolvePointGroup(
                            newCarryOverPlayer, pointGroupCounter + 1, tournament);

                    // Again, continue if the list is good or there are no other
                    // options
                    if (!isCarryOver || carryOverPlayerIndex == 0 || !Match.hasDuplicate(nextPointGroupMatches)) {

                        returnedMatches.addAll(nextPointGroupMatches);

                        return returnedMatches;

                    }
                }
            }
        }
    }

    private List<Match> getRandomMatches(Player carryOverPlayer, List<Player> players, TournamentDetails tournamentDetails) {

        List<Match> matches = new ArrayList<>();

        // if there are no players, return no matches
        if (players.isEmpty()) {
            return matches;
        }

        Match m = new Match(null,null,null);

        List<Match> subMatches = new ArrayList<>();

        // If there is a carry over player, they are always player 1
        if (carryOverPlayer != null) {

            m.setPlayer1(carryOverPlayer);

            for (int counter = 0; counter < players.size(); counter++) {

                m.setPlayer2(players.get(counter));
                m.checkDuplicate(tournamentDetails.getRounds());

                // Continue if the match is not a duplicate or this is the last
                // chance
                if (!m.isDuplicate() || counter == players.size() - 1) {

                    List<Player> nextPlayers = new ArrayList<>(players);
                    nextPlayers.remove(m.getPlayer2());

                    subMatches = getRandomMatches(null, nextPlayers, tournamentDetails);

                    // if no duplicates, stop, else try again
                    if (!Match.hasDuplicate(subMatches)) {
                        matches.add(m);
                        matches.addAll(subMatches);
                        return matches;
                    }
                }
            }
        } else {

            // this is the same as the previous one, except the first player in
            // the list is player 1 and both player 1 and 2 must be removed from
            // the continuing list

            m.setPlayer1(players.get(0));

            // Loop through each other player to find a match
            for (int counter = 1; counter < players.size(); counter++) {

                // add new player and check if it is valid
                m.setPlayer2(players.get(counter));
                m.checkDuplicate(tournamentDetails.getRounds());

                // Continue if the match is not a duplicate or this is the last
                // chance
                if (!m.isDuplicate() || counter == players.size() - 1) {

                    // Create the list of remaining players
                    List<Player> nextPlayers = new ArrayList<>(players);
                    nextPlayers.remove(m.getPlayer1());
                    nextPlayers.remove(m.getPlayer2());

                    // Call function recursively
                    subMatches = getRandomMatches(null, nextPlayers, tournamentDetails);

                    // if no duplicates, stop, else try again
                    if (!Match.hasDuplicate(subMatches)) {
                        m.setId(matches.size() + 1);
                        matches.add(m);
                        matches.addAll(subMatches);
                        return matches;
                    }
                }
            }

        }

        m.setId(matches.size() + 1);
        // If we got here than we gave up, there was no chance of finding a non
        // duplicate group
        matches.add(m);
        matches.addAll(subMatches);
        return matches;
    }

    public void matchResult(Long tournamentId, List<MatchResultRequest> results) throws TournamentDetailsParseException, NotFoundException, MatchResultException {

        Tournament tournament = tournamentService.findById(tournamentId);

        Round round = roundService.findRound(tournament.getTournamentDetails());

        for (MatchResultRequest r : results) processResult(round, r);

        results.forEach(ConsumerWrapper.wrap(r -> processResult(round, r)));

        tournamentService.update(tournament);

    }

    private void processResult(Round round, MatchResultRequest request) throws MatchResultException {

        Match match = findMatch(round, request.getMatch());

        if(match.isBye())
            throw new MatchResultException();

        validatePlayerResult(request.getPlayer1Result(), request.getPlayer2Result());

        addResult(match, request.getPlayer1(), request.getPlayer1Result());
        addResult(match, request.getPlayer2(), request.getPlayer2Result());

        validateMatchResult(match);

    }

    private void validateMatchResult(Match match) {
        MatchResultEnum resultEnum = null;

        if(match.getPlayer1Points() > match.getPlayer2Points()){

            resultEnum = MatchResultEnum.PLAYER_1_WINS;

        }else if(match.getPlayer2Points() > match.getPlayer1Points()){

            resultEnum = MatchResultEnum.PLAYER_2_WINS;

        }else if(match.getPlayer1Points().equals(match.getPlayer2Points())){

            resultEnum = MatchResultEnum.DRAW;

        }

        match.setMatchResultEnum(resultEnum);

    }

    private void validatePlayerResult(Integer player1Result, Integer player2Result) throws MatchResultException {

        if(player1Result == null || player1Result < 0)
            throw new MatchResultException();

        if(player2Result == null || player2Result < 0)
            throw new MatchResultException();

    }

    private void addResult(Match match, Long player, Integer playerResult) {

        if(match.getPlayer1().getId().equals(player)){

            match.setPlayer1Points(playerResult);

        }else if(match.getPlayer2().getId().equals(player)){

            match.setPlayer2Points(playerResult);

        }

    }

    Match findMatch(Round r, Integer matchId) {

        return r.getMatches().stream().filter(m -> m.getId().equals(matchId)).findAny().orElse(null);

    }

    List<Match> getCompletedPlayerMatches(Player player, Tournament t) {

        List<Match> matches = new ArrayList<>();

        if (t != null) {

            for (Round r : t.getTournamentDetails().getRounds()) {
                matches = r.getMatches().stream().filter(m -> playerMatchComplete(m, player)).collect(Collectors.toList());
            }

        }

        return matches;
    }

    private boolean playerMatchComplete(Match m, Player p) {
        return (
                (m.getPlayer1().equals(p) || (m.getPlayer2() != null && m.getPlayer2().equals(p)))
                        && (m.isComplete())
        );
    }

}


