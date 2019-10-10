package com.uaito.controllers;

import com.uaito.dto.Messages;
import com.uaito.exception.*;
import com.uaito.request.MatchResultRequest;
import com.uaito.request.PlayerRequest;
import com.uaito.request.TournamentRequest;
import com.uaito.service.MatchService;
import com.uaito.service.PlayerService;
import com.uaito.service.RoundService;
import com.uaito.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private MatchService matchService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TournamentRequest request) {

        try {

            tournamentService.insert(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Messages.CREATED);

        } catch (TournamentNameExistException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.TOURNAMENT_NAME_EXIST);

        }

    }

    @GetMapping()
    public ResponseEntity<?> getAll() {

        try {

            return ResponseEntity.status(HttpStatus.OK).body(tournamentService.findAll());

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);

        }

    }

    @GetMapping("/created/{created}")
    public ResponseEntity<?> getByCreated(@PathVariable(value = "created") Long created) {

        try{
            return ResponseEntity.status(HttpStatus.OK).body(tournamentService.findByCreated(created));

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);

        }

    }

    @PostMapping("/player")
    public ResponseEntity<?> addPlayer(@RequestBody PlayerRequest player,
                                       @RequestHeader(value = "tournamentId") Long tournamentId){

        try {

            playerService.addPlayer(player, tournamentId);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        } catch (PlayerExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.PLAYER_EXIST);
        } catch (TournamentTicketException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Messages.TOURNAMENT_TICKET_EXCEEDED);
        }

    }

    @PostMapping("/swapPlayer")
    public ResponseEntity<?> swapPlayer(
            @RequestHeader(value = "tournamentId") Long tournamentId,
            @RequestHeader(value = "created") Long created,
            @RequestHeader(value = "player1") Long player1,
            @RequestHeader(value = "player1Match") Integer player1Match,
            @RequestHeader(value = "player2") Long player2,
            @RequestHeader(value = "player2Match") Integer player2Match){

        try {

            playerService.swapPlayer(tournamentId, created, player1, player1Match, player2, player2Match);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        }

    }

    @PostMapping("/matchResult")
    public ResponseEntity<?> matchResult(
            @RequestHeader(value = "tournamentId") Long tournamentId,
            @RequestBody List<MatchResultRequest> results){

        try {

            matchService.matchResult(tournamentId, results);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        } catch (MatchResultException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        }

    }

    @GetMapping("/nextRound/{tournamentId}")
    public ResponseEntity<?> nextRound(@PathVariable(value = "tournamentId") Long tournamentId,
                                       @RequestHeader(value = "created") Long created){

        try {

            return ResponseEntity.status(HttpStatus.OK).body(roundService.nextRound(tournamentId, created));

        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        } catch (RoundNotFinishException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Messages.ROUND_NOT_FINISH);
        }
    }

    @PostMapping("/cancelRound")
    public ResponseEntity<?> cancelRound(@RequestHeader(value = "tournamentId") Long tournamentId,
                                       @RequestHeader(value = "created") Long created,
                                         @RequestHeader(value = "round") Integer round){

        try {

            roundService.cancelRound(tournamentId, created, round);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        }
    }

    @GetMapping("/ranking/{tournamentId}")
    public ResponseEntity<?> ranking(@PathVariable(value = "tournamentId") Long tournamentId){

        try {

            return ResponseEntity.status(HttpStatus.OK).body(tournamentService.ranking(tournamentId));

        } catch (TournamentDetailsParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.DEFAULT);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);
        }
    }
}
