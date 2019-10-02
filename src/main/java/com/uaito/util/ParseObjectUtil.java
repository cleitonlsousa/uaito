package com.uaito.util;

import com.uaito.domain.Account;
import com.uaito.domain.Tournament;
import com.uaito.dto.Player;
import com.uaito.dto.TournamentDetails;
import com.uaito.enuns.YesNoEnum;
import com.uaito.exception.NotFoundException;
import com.uaito.exception.TournamentDetailsParseException;
import com.uaito.request.AccountRequest;
import com.uaito.request.PlayerRequest;
import com.uaito.request.TournamentRequest;
import com.uaito.response.AccountResponse;
import com.uaito.response.TournamentResponse;
import com.uaito.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParseObjectUtil {

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private AccountService accountService;

    public TournamentResponse deParaResponse(Tournament t) throws NotFoundException, TournamentDetailsParseException {

        TournamentResponse response = new TournamentResponse();

        response.setDateTime(t.getDateTime());
        response.setDefaultRoundLength(t.getDefaultRoundLength());
        response.setId(t.getId());
        response.setLocation(t.getLocation());
        response.setName(t.getName());
        response.setPointsSize(t.getPointsSize());
        response.setRounds(t.getRounds());
        response.setTickets(t.getTickets());

        response.setCreated(accountService.findByIdOrEmail(t.getCreated().toString()));

        if(t.getDetails() != null)
            response.setDetails((TournamentDetails) jsonUtil.parse(t.getDetails(), TournamentDetails.class));

        return response;
    }

    public Tournament deParaTournament(TournamentRequest request) {

        Tournament tournament = new Tournament();
        tournament.setCreated(request.getCreated());
        tournament.setDateTime(request.getDateTime());
        tournament.setDefaultRoundLength(request.getDefaultRoundLength());
        tournament.setLocation(request.getLocation());
        tournament.setName(request.getName());
        tournament.setPointsSize(request.getPointsSize());
        tournament.setRounds(request.getRounds());
        tournament.setTickets(request.getTickets() == null ? 0 : request.getTickets());

        return tournament;
    }

    public AccountResponse deParaResponse(Account a) {

        AccountResponse response = new AccountResponse();
        response.setEmail(a.getEmail());
        response.setFirstName(a.getFirstName());
        response.setId(a.getId());
        response.setLastName(a.getLastName());

        return  response;

    }

    public Account deParaAccount(AccountRequest accountRequest) {
        Account account = new Account();
        account.setEmail(accountRequest.getEmail());
        account.setFirstName(accountRequest.getFirstName());
        account.setLastName(accountRequest.getLastName());
        account.setPassword(accountRequest.getPassword());

        if(account.getId() == null)
            account.setActive(YesNoEnum.YES);

        return account;

    }

    public Player deParaPlayer(PlayerRequest playerRequest, Account account) {

        Player player = new Player();
        player.setId(account.getId());
        player.setFirstName(account.getFirstName());
        player.setLastName(account.getLastName());
        player.setActive(playerRequest.getActive());
        player.setFactionEnum(playerRequest.getFactionEnum());
        player.setFirstRoundBye(playerRequest.getFirstRoundBye());

        return player;

    }
}
