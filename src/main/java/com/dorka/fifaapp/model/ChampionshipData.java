package com.dorka.fifaapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class ChampionshipData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer round;
    private boolean ongoingChampionship = true;
    private boolean ongoingRound = false;
    private boolean ongoingTeamSelection = true;
    private Timestamp startTime;
    private String winnerName;

    public ChampionshipData() {
        startTime = new Timestamp(System.currentTimeMillis());
    }
}
