package com.dorka.fifaapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    public Match(MatchKey key, Team homeTeam, Team awayTeam) {
        id = key;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }
}
