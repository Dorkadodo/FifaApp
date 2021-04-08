package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.InvalidNumberOfTeamsException;
import com.dorka.fifaapp.model.Match;
import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.Team;
import com.dorka.fifaapp.repo.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class ChampionshipService {

    private TeamService teamService;
    private PlayerService playerService;
    private MatchRepository matchRepository;

    @Autowired
    public ChampionshipService(TeamService teamService, PlayerService playerService, MatchRepository matchRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.matchRepository = matchRepository;
    }

    public List<Match> draw() throws InvalidNumberOfTeamsException {
        List<Team> teams = teamService.getAllTeams();
        List<Player> players = playerService.getAllPlayers();
        checkIfPowerOfTwo(teams.size());
        players.stream()
                .max(Comparator.comparing(player -> player.getOwnTeams().size()));

    }

    private void checkIfPowerOfTwo(Integer num) throws InvalidNumberOfTeamsException {
        if (num == 2) {
            return;
        }
        if (num % 2 == 1 || num <= 0) {
            throw new InvalidNumberOfTeamsException();
        }
        checkIfPowerOfTwo(num / 2);
    }
}
