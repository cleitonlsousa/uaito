package com.uaito.service;

import com.uaito.comparator.Comparator;
import com.uaito.comparator.TournamentComparator;
import com.uaito.domain.Tournament;
import com.uaito.dto.Messages;
import com.uaito.dto.Player;
import com.uaito.dto.TournamentDetails;
import com.uaito.exception.*;
import com.uaito.repository.TournamentRepository;
import com.uaito.request.TournamentRequest;
import com.uaito.response.TournamentResponse;
import com.uaito.util.JsonUtil;
import com.uaito.util.ParseObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.uaito.exception.ProducerWrapper.wrap;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private ParseObjectUtil parseObjectUtil;

    public Tournament findById(Long id) throws NotFoundException, TournamentDetailsParseException {

        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

        parse(tournament);

        return tournament;

    }

    public Tournament findByIdAndCreated(Long id, Long created) throws NotFoundException, TournamentDetailsParseException {

        Tournament tournament = tournamentRepository.
                findByIdAndCreated(id, created).orElseThrow(() -> new NotFoundException(id.toString()));

        parse(tournament);

        return tournament;

    }

    private void parse(Tournament tournament) throws NotFoundException, TournamentDetailsParseException {

        if(tournament.getDetails() != null) {

            tournament.setTournamentDetails((TournamentDetails) jsonUtil.parse(tournament.getDetails(), TournamentDetails.class));

        }else{

            tournament.setTournamentDetails(new TournamentDetails());

        }

    }

    public List<TournamentResponse> findByCreated(Long created) throws NotFoundException {

        List<Tournament> tournamentList = tournamentRepository.findByCreated(created);

        if(CollectionUtils.isEmpty(tournamentList))
            throw new NotFoundException(Messages.NOT_FOUND.getMessage());

        return tournamentList.stream().map(t -> wrap(()-> parseObjectUtil.deParaResponse(t))).collect(Collectors.toList());

    }

    public List<TournamentResponse> findAll() throws NotFoundException {

        List<Tournament> tournamentList = tournamentRepository.findAll();

        if(CollectionUtils.isEmpty(tournamentList))
            throw new NotFoundException(Messages.NOT_FOUND.getMessage());

        return tournamentList.stream().map(t -> wrap(()-> parseObjectUtil.deParaResponse(t))).collect(Collectors.toList());

    }

    public void update(Tournament tournament) throws TournamentDetailsParseException {

        if(tournament.getTournamentDetails() != null)
            tournament.setDetails(jsonUtil.convert(tournament.getTournamentDetails()));

        tournamentRepository.save(tournament);

    }

    public void insert(TournamentRequest request) throws TournamentNameExistException {

        try {

            tournamentRepository.save(parseObjectUtil.deParaTournament(request));

        }catch (DataIntegrityViolationException d){

            throw new TournamentNameExistException(Messages.TOURNAMENT_NAME_EXIST.getMessage());

        }

    }

    public TournamentComparator<Player> getPairingComparator(Tournament t) {
        return new Comparator(t, Comparator.pairingCompare, playerService);
    }
}
