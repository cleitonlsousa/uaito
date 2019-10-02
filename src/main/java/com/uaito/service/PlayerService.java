package com.uaito.service;

import com.uaito.Constants;
import com.uaito.domain.Account;
import com.uaito.domain.Tournament;
import com.uaito.dto.Match;
import com.uaito.dto.Player;
import com.uaito.dto.Round;
import com.uaito.dto.TournamentDetails;
import com.uaito.exception.*;
import com.uaito.request.PlayerRequest;
import com.uaito.util.JsonUtil;
import com.uaito.util.ParseObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private ParseObjectUtil parseObjectUtil;

    public void addPlayer(PlayerRequest player, Long tournamentId) throws NotFoundException, TournamentDetailsParseException, PlayerExistException, TournamentTicketException {

        Tournament tournament = tournamentService.findById(tournamentId);

        TournamentDetails tournamentDetails;

        if(tournament.getDetails() != null) {
            tournamentDetails = (TournamentDetails) jsonUtil.parse(tournament.getDetails(), TournamentDetails.class);
        }else{
            tournamentDetails = new TournamentDetails();
        }

        if(CollectionUtils.isEmpty(tournamentDetails.getPlayers()))
            tournamentDetails.setPlayers(new ArrayList<>());

        if(tournamentDetails.getPlayers().size() >= tournament.getTickets())
            throw new TournamentTicketException();

        if(tournamentDetails.getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId())))
            throw new PlayerExistException();

        Account account = accountService.findById(player.getId());

        tournamentDetails.getPlayers().add(parseObjectUtil.deParaPlayer(player, account));

        tournament.setDetails(jsonUtil.convert(tournamentDetails));

        tournamentService.update(tournament);

    }

    public void swapPlayer(Long tournamentId, Long created, Long player1, Integer player1Match,
                           Long player2, Integer player2Match) throws NotFoundException, TournamentDetailsParseException, SwapPlayerException {

        Tournament tournament = tournamentService.findByIdAndCreated(tournamentId, created);

        TournamentDetails details = (TournamentDetails) jsonUtil.parse(tournament.getDetails(), TournamentDetails.class);

        Round round = findRound(details);

        if(round == null)
            throw new SwapPlayerException();

        validateMatch(round, player1, player1Match, player2, player2Match);

        tournament.setDetails(jsonUtil.convert(details));

        tournamentService.update(tournament);
    }

    private void validateMatch(Round r, Long player1, Integer player1Match, Long player2, Integer player2Match) throws SwapPlayerException {

        Match match1 = findMatch(r, player1Match);
        Match match2 = findMatch(r, player2Match);

        Player p1 = findPlayer(match1, player1);
        Player p2 = findPlayer(match2, player2);

        changePlayer(match1, p1, p2);
        changePlayer(match2, p2, p1);

    }

    private Round findRound(TournamentDetails details) {

        return details.getRounds().stream().filter(r -> !r.isComplete()).findAny().orElse(null);

    }

    private Match findMatch(Round r, Integer matchId) throws SwapPlayerException {

        Match match = r.getMatches().stream().filter(m -> m.getId() == matchId).findAny().orElse(null);

        if(match.isComplete())
            throw new SwapPlayerException();

        return match;

    }

    private Player findPlayer(Match m, Long player) {

        return (m.getPlayer1().getId() == player) ? m.getPlayer1() :
            (m.getPlayer2().getId() == player) ? m.getPlayer2() : null;

    }

    private void changePlayer(Match m, Player matchPlayer, Player newPlayer) {

        if(m.getPlayer1().getId() == matchPlayer.getId()){
            m.setPlayer1(newPlayer);
        }else if( (m.getPlayer2() != null) &&  (m.getPlayer2().getId() == matchPlayer.getId()) ){
            m.setPlayer2(newPlayer);
        }

    }

    public int playerScore(Player p, List<Round> rounds){

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
