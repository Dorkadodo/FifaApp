package com.dorka.fifaapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MatchKey implements Serializable {

    @Column(name = "HOMETEAM_ID")
    private Long hometeamId;

    @Column(name = "AWAYTEAM_ID")
    private Long awayteamId;

    @Column(name = "ROUND_NUMBER")
    private Long roundNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchKey matchKey = (MatchKey) o;
        return hometeamId.equals(matchKey.hometeamId) && awayteamId.equals(matchKey.awayteamId) && roundNumber.equals(matchKey.roundNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hometeamId, awayteamId, roundNumber);
    }
}
