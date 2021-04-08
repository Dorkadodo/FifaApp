package com.dorka.fifaapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(unique = true)
    private String name;

    @OneToMany (mappedBy = "owner")
    private List<Team> ownTeams;

    public Player() {
    }

    public Player(String name){
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

    public List<Team> getOwnTeams() {
        return ownTeams;
    }

    public void setOwnTeams(List<Team> ownTeams) {
        this.ownTeams = ownTeams;
    }
    //endregion
}
