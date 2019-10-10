package com.uaito.service;

import com.uaito.Constants;
import com.uaito.domain.Account;
import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Messages;
import com.uaito.dto.Player;
import com.uaito.dto.Round;
import com.uaito.exception.NotFoundException;
import com.uaito.exception.PlayerExistException;
import com.uaito.exception.TournamentDetailsParseException;
import com.uaito.exception.TournamentTicketException;
import com.uaito.request.PlayerRequest;
import com.uaito.util.JsonUtil;
import com.uaito.util.ParseObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private ParseObjectUtil parseObjectUtil;


    public void addPlayer(PlayerRequest player, Long tournamentId) throws NotFoundException, TournamentDetailsParseException, PlayerExistException, TournamentTicketException {

        Tournament tournament = tournamentService.findById(tournamentId);

        if(CollectionUtils.isEmpty(tournament.getTournamentDetails().getPlayers()))
            tournament.getTournamentDetails().setPlayers(new ArrayList<>());

        if(tournament.getTournamentDetails().getPlayers().size() >= tournament.getTickets())
            throw new TournamentTicketException();

        if(tournament.getTournamentDetails().getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId())))
            throw new PlayerExistException();

        Account account = accountService.findById(player.getId());

        tournament.getTournamentDetails().getPlayers().add(parseObjectUtil.deParaPlayer(player, account));

        tournamentService.update(tournament);

    }

    public void swapPlayer(Long tournamentId, Long created, Long player1, Integer player1Match,
                           Long player2, Integer player2Match) throws NotFoundException, TournamentDetailsParseException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        Round round = roundService.findRound(tournament.getTournamentDetails());

        Match match1 = matchService.findMatch(round, player1Match);
        Match match2 = matchService.findMatch(round, player2Match);

        Player p1 = findPlayer(match1, player1);
        Player p2 = findPlayer(match2, player2);

        if(p1 == null || p2 == null)
            throw new NotFoundException(Messages.NOT_FOUND.getMessage());

        changePlayer(match1, p1, p2);
        changePlayer(match2, p2, p1);

        tournament.setDetails(jsonUtil.convert(tournament.getTournamentDetails()));

        tournamentService.update(tournament);
    }

    private Player findPlayer(Match m, Long player) {

        return (m.getPlayer1().getId().equals(player)) ? m.getPlayer1() :
            (m.getPlayer2().getId().equals(player)) ? m.getPlayer2() : null;

    }

    private void changePlayer(Match m, Player matchPlayer, Player newPlayer) {

        if(m.getPlayer1().getId().equals(matchPlayer.getId())){

            m.setPlayer1(newPlayer);

        }else if( (m.getPlayer2() != null) &&  (m.getPlayer2().getId().equals(matchPlayer.getId())) ){

            m.setPlayer2(newPlayer);

        }

    }

    public int getScore(Player p, Tournament t){

        int score = 0;

        for (Match match : getPlayerMatches(p,t)) {

            if (match.isWinner(p)) {

                score += Constants.WIN_POINTS;

            } else if (match.isBye()) {

                score += Constants.BYE_POINTS;

            } else {

                score += Constants.LOSS_POINTS;

            }

        }

        return score;
    }

    public List<Match> getPlayerMatches(Player player, Tournament t) {

        List<Match> matches = new ArrayList<>();

        if (t != null && player != null) {

            t.getTournamentDetails().getRounds().forEach(r -> r.getMatches().forEach(m -> {
                if (
                        (m.getPlayer1().equals(player)) ||
                                (m.getPlayer2() != null && m.getPlayer2().equals(player))
                )
                    matches.add(m);
            }));

        }

        return matches;

    }

    public boolean isHeadToHeadWinner(Player player, Tournament tournament) {

        int h2hWinner = 1;

        if (tournament != null) {

            int score = getScore(player,tournament);

            List<Player> players = tournament.getTournamentDetails().getPlayers().stream()
                    .filter(p -> score == getScore(p,tournament)).collect(Collectors.toList());

            if (players.isEmpty()) {

                h2hWinner = 0;

            } else {

                playerLoop: for (Player p : players) {
                    for (Match m : getPlayerMatches(p,tournament)) {

                        if (m.isWinner(player)) {
                            continue playerLoop;
                        }
                    }
                    h2hWinner = 0;
                }
            }
        }

        return h2hWinner == 1;
    }

    public double getAverageSoS(Player player, Tournament tournament) {

        double sos = 0.0;

        List<Match> matches = matchService.getCompletedPlayerMatches(player, tournament);

        int numOpponents = 0;

        for (Match m : matches) {

            if (!m.isBye() && m.getWinner() != null) {

                if (m.getPlayer1().equals(player)) {

                    sos += getAverageScore(m.getPlayer2(), tournament);
                    numOpponents++;

                } else {
                    sos += getAverageScore(m.getPlayer1(), tournament);
                    numOpponents++;
                }
            }
        }

        // if they don't have any opponents recorded yet, don't divide by 0
       double averageSos = numOpponents > 0 ? sos / numOpponents : 0;

        if (!Double.isNaN(averageSos)) {
            BigDecimal bd = new BigDecimal(averageSos);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            averageSos = bd.doubleValue();
        }

        return averageSos;
    }

    private double getAverageScore(Player p, Tournament t) {

        int score = getScore(p,t);
        int matchCount = matchService.getCompletedPlayerMatches(p, t).size();

        return score * 1.0 / matchCount;

    }

    public int getMarginOfVictory(Tournament t, Player p) {

        Integer movPoints = 0;

        movPoints = 0;

        for (Match match : getPlayerMatches(p,t)) {

            Integer tournamentPoints = t.getPointsSize();

            if (match.isBye()) {
                movPoints += t.getByePoints();
                continue;
            } else if (match.getWinner() == null) {
                continue;
            }

            boolean isPlayer1 = match.getPlayer1().equals(p);

            int player1Points = match.getPlayer1Points() == null ? 0 : match.getPlayer1Points();
            int player2Points = match.getPlayer2Points() == null ? 0 : match.getPlayer2Points();

            int diff = player1Points - player2Points;

            movPoints += isPlayer1 ? tournamentPoints + diff : tournamentPoints - diff;
        }

        return movPoints;
    }

    public int getWins(Tournament t, Player p) {

        return (int)getPlayerMatches(p,t).stream().filter(match -> (match.isWinner(p) || match.isBye())).count();
    }

    public int getLosses(Tournament t, Player p) {

        return (int)getPlayerMatches(p,t).stream().filter(match -> (!match.isWinner(p))).count();

    }

    public int getByes(Tournament t, Player p) {

        return (int)getPlayerMatches(p,t).stream().filter(Match::isBye).count();
    }

}
