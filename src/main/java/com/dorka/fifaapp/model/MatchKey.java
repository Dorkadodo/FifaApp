package com.dorka.fifaapp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MatchKey implements Serializable {

    @Column(name = "HOMETEAM_ID")
    private Long hometeamId;

    @Column(name = "AWAYTEAM_ID")
    private Long awayteamId;

    public MatchKey() {
    }

    public Long getHometeamId() {
        return hometeamId;
    }

    public void setHometeamId(Long hometeamId) {
        this.hometeamId = hometeamId;
    }

    public Long getAwayteamId() {
        return awayteamId;
    }

    public void setAwayteamId(Long awayteamId) {
        this.awayteamId = awayteamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchKey)) return false;
        MatchKey matchKey = (MatchKey) o;
        return getHometeamId().equals(matchKey.getHometeamId()) &&
                getAwayteamId().equals(matchKey.getAwayteamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHometeamId(), getAwayteamId());
    }
}
