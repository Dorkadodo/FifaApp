package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.InvalidNumberOfTeamsException;
import com.dorka.fifaapp.exception.NoPlayerFoundException;
import com.dorka.fifaapp.model.ChampionshipData;
import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.Team;

import java.util.HashMap;
import java.util.List;

public interface DrawInterface {
    void drawOfNewChampionship(ChampionshipData championshipData)
        throws NoPlayerFoundException;

    void createDraw(Integer round, HashMap<Player, List<Team>> awayTeamMap) throws NoPlayerFoundException;

    default void checkIfPowerOfTwo(Integer num) throws InvalidNumberOfTeamsException {
        if (num == 2) {
            return;
        }
        if (num % 2 == 1 || num <= 0) {
            throw new InvalidNumberOfTeamsException();
        }
        checkIfPowerOfTwo(num / 2);
    }
}
