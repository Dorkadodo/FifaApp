package com.dorka.fifaapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
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
    @Column(nullable = false)
    private boolean ongoingChampionship = true;
    @Column(nullable = false)
    private boolean ongoingRound = false;
    private Timestamp startTime;
    private String winnerName;

    public ChampionshipData() {
        startTime = new Timestamp(System.currentTimeMillis());
    }
}
