package com.uaito.domain;

import com.uaito.dto.TournamentDetails;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
public class Tournament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long created;

    private String name;
    private String location;
    private Integer rounds;
    private LocalDate date;

    @Column(name = "points_size")
    private Integer pointsSize;

    @Column(name = "default_round_length")
    private Integer defaultRoundLength;
    private Integer tickets;

    private String details;

    @Transient
    private TournamentDetails tournamentDetails;

}
