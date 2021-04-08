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
    @MapsId("hometeamId")
    @JoinColumn(name = "HOMETEAM_ID")
    private Team hometeamId;

    @ManyToOne
    @MapsId("awayteamId")
    @JoinColumn(name = "AWAYTEAM_ID")
    private Team awayteamId;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
