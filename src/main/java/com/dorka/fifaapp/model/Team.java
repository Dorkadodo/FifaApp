package com.dorka.fifaapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private Player owner;

    @OneToMany(mappedBy = "awayTeam")
    private List<Match> homeTeamMatches;

    @OneToMany (mappedBy = "homeTeam")
    private List<Match> awayTeamMatches;

    public Team() {
        homeTeamMatches = null;
        awayTeamMatches = null;
    }

    public Team(String name, Player owner) {
        this.owner = owner;
        this.name = name;
    }
}
