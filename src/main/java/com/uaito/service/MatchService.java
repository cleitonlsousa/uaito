package com.uaito.service;

import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Player;
import com.uaito.dto.TournamentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class MatchService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerService playerService;

    private List<List<Player>> pointGroups;

    public List<Match> generateMatches(Tournament tournament, TournamentDetails details) {

        if(CollectionUtils.isEmpty(details.getPlayers()))
            return new ArrayList<>();

        if(details.getRounds().isEmpty())
            return firstRoundPairings(details.getPlayers());

        getPointGroups(tournament, details);

        List<Match> matches = null;

        if(pointGroups.isEmpty() == false) {
            matches = resolvePointGroup(null, 0, tournament, details);
        }

        return matches;
    }

    protected List<Match> firstRoundPairings(List<Player> playersList) {
        List<Match> matches;
        List<Player> nonPairedPlayers = new ArrayList<>();
        nonPairedPlayers.addAll(playersList);

        List<Player> firstRoundByePlayers = new ArrayList<>();
        for (Player p : nonPairedPlayers) {
            if (p.isFirstRoundBye()) {
                firstRoundByePlayers.add(p);
            }
        }
        nonPairedPlayers.removeAll(firstRoundByePlayers);

        matches = initialRandom(nonPairedPlayers);

        int i = matches.size() + 1;
        for (Player p : firstRoundByePlayers) {
            matches.add(new Match(i++, p, null));
        }

        return matches;
    }

    private void getPointGroups(Tournament tournament, TournamentDetails tournamentDetails){

        TreeMap<Integer, List<Player>> playerMap = new TreeMap<>(new Comparator<Integer>() {

                    @Override
                    public int compare(Integer arg0, Integer arg1) {
                        return arg0.compareTo(arg1) * -1;
                    }
                });

        for (Player p : tournamentDetails.getPlayers()) {

            Integer points = playerService.playerScore(p, tournamentDetails.getRounds());

            List<Player> pointGroup = playerMap.get(points);

            if (pointGroup == null) {
                pointGroup = new ArrayList<>();
                playerMap.put(points, pointGroup);
            }

            pointGroup.add(p);
        }

        pointGroups = new ArrayList<>();
        for(Integer i : playerMap.keySet()){
            pointGroups.add(playerMap.get(i));
        }
    }

    private List<Match> resolvePointGroup(Player carryOverPlayer, int pointGroupCounter, Tournament tournament,
                                          TournamentDetails tournamentDetails) {

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

            List<Player> tempList = new ArrayList<>();
            tempList.addAll(playerList);

            if (isCarryOver) {
                carryOverPlayerIndex--;
                newCarryOverPlayer = playerList.get(carryOverPlayerIndex);
                tempList.remove(newCarryOverPlayer);
            }

            List<Match> returnedMatches = getRandomMatches(carryOverPlayer, tempList, tournament, tournamentDetails);

            // If the list was good or if there was no carry over players that
            // can change things up
            if (isCarryOver == false || carryOverPlayerIndex == 0
                    || Match.hasDuplicate(returnedMatches) == false) {

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
                            newCarryOverPlayer, pointGroupCounter + 1, tournament, tournamentDetails);

                    // Again, continue if the list is good or there are no other
                    // options
                    if (isCarryOver == false
                            || carryOverPlayerIndex == 0
                            || Match.hasDuplicate(nextPointGroupMatches) == false) {
                        returnedMatches.addAll(nextPointGroupMatches);
                        return returnedMatches;
                    }
                }
            }
        }
    }

    /**
     * Recursive call to find a non duplicate match of remaining players in a
     * group
     *
     * @param carryOverPlayer
     * @param players
     * @return
     */
    private List<Match> getRandomMatches(Player carryOverPlayer, List<Player> players, Tournament tournament,
                                         TournamentDetails tournamentDetails) {

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
                if (m.isDuplicate() == false || counter == players.size() - 1) {
                    List<Player> nextPlayers = new ArrayList<Player>();
                    nextPlayers.addAll(players);
                    nextPlayers.remove(m.getPlayer2());
                    subMatches = getRandomMatches(null, nextPlayers, tournament, tournamentDetails);

                    // if no duplicates, stop, else try again
                    if (Match.hasDuplicate(subMatches) == false) {
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
                if (m.isDuplicate() == false || counter == players.size() - 1) {

                    // Create the list of remaining players
                    List<Player> nextPlayers = new ArrayList<Player>();
                    nextPlayers.addAll(players);
                    nextPlayers.remove(m.getPlayer1());
                    nextPlayers.remove(m.getPlayer2());

                    // Call function recursively
                    subMatches = getRandomMatches(null, nextPlayers, tournament, tournamentDetails);

                    // if no duplicates, stop, else try again
                    if (Match.hasDuplicate(subMatches) == false) {
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

    public List<Match> initialRandom(List<Player> playersList) {

        List<Player> players = new ArrayList<>(playersList);

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

            Match match = new Match(i++, player1, player2);
            matches.add(match);
        }
        return matches;
    }

}
