package com.dorka.fifaapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private Player owner;

    @OneToMany(mappedBy = "awayteamId")
    private List<Match> hometeamMatches;

    @OneToMany (mappedBy = "hometeamId")
    private List<Match> awayteamMatches;

    public Team() {
        hometeamMatches = null;
        awayteamMatches = null;
    }

    public Team(String name, Player owner) {
        this.owner = owner;
        this.name = name;
    }

    //region GettersAndSetters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    //endregion
}
