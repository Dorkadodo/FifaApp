package com.dorka.fifaapp.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Match{

    @EmbeddedId
    private MatchKey id;

    @ManyToOne
    @MapsId("homeTeamId")
    @JoinColumn(name = "HOME_TEAM_ID")
    private Team homeTeam;

    @ManyToOne
    @MapsId("awayTeamId")
    @JoinColumn(name = "AWAY_TEAM_ID")
    private Team awayTeam;

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    @ManyToOne
    @JoinColumn(name = "WINNER_ID")
    private Team winner;

    public Match(MatchKey key, Team homeTeam, Team awayTeam) {
        id = key;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }
}
