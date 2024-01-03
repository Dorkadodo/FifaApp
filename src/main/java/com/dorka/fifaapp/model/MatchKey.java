package com.dorka.fifaapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MatchKey implements Serializable {

    @Column(name = "HOME_TEAM_ID")
    private Long homeTeamId;

    @Column(name = "AWAY_TEAM_ID")
    private Long awayTeamId;

    @Column(name = "ROUND_NUMBER")
    private Integer roundNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchKey matchKey = (MatchKey) o;
        return homeTeamId.equals(matchKey.homeTeamId) && awayTeamId.equals(matchKey.awayTeamId) && roundNumber.equals(matchKey.roundNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeamId, awayTeamId, roundNumber);
    }
}
