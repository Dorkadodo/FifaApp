package com.dorka.fifaapp.model;

import javax.persistence.*;

@Entity
public class Match{

    @EmbeddedId
    private MatchKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hometeamId")
    @JoinColumn(name = "HOMETEAM_ID")
    private Team hometeamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("awayteamId")
    @JoinColumn(name = "AWAYTEAM_ID")
    private Team awayteamId;
    private Integer homeTeamScore;
    private Integer awayTeamScore;

    public Match() {
    }

    //region GettersAndSetters

    public MatchKey getId() {
        return id;
    }

    public void setId(MatchKey id) {
        this.id = id;
    }

    public Team getHometeamId() {
        return hometeamId;
    }

    public void setHometeamId(Team homeTeamId) {
        this.hometeamId = homeTeamId;
    }

    public Team getAwayteamId() {
        return awayteamId;
    }

    public void setAwayteamId(Team awayTeamId) {
        this.awayteamId = awayTeamId;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }
}
