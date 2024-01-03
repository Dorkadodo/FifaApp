package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.NoPlayerFoundException;
import com.dorka.fifaapp.model.*;
import com.dorka.fifaapp.repo.MatchRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class DrawFuns implements DrawInterface {

    private final PlayerService playerService;
    private final MatchRepository matchRepository;

    public DrawFuns(PlayerService playerService, MatchRepository matchRepository) {
        this.playerService = playerService;
        this.matchRepository = matchRepository;
    }

    public void drawOfNewChampionship(ChampionshipData championshipData) throws NoPlayerFoundException {
        createDraw(championshipData.getRound(), getNewTeamsInMap());
    }

//    public List<Match> redraw() {
//
//    }

    public void createDraw(Integer round, HashMap<Player, List<Team>> awayTeamMap) throws NoPlayerFoundException {
        List<Player> players = new ArrayList<>(awayTeamMap.keySet());
        List<Match> matchList = new ArrayList<>();

        while (!awayTeamMap.isEmpty()) {
            assignTeamsOfOnePlayer(round, players, awayTeamMap, matchList);
        }

        matchRepository.saveAll(matchList);
    }

    //region DrawPrivateMethods
    private void assignTeamsOfOnePlayer(
        Integer round,
        List<Player> awayPlayers,
        HashMap<Player, List<Team>> awayTeamMap,
        List<Match> matchList
    ) throws NoPlayerFoundException {

        Player homeTeamPlayer = getHomeTeamPlayer(awayTeamMap, awayPlayers);
        List<Team> homeTeamList = getHomeTeamList(awayTeamMap, homeTeamPlayer);
        int availableTeamCount = getAvailableTeamCount(awayTeamMap);
        int createdMatchCounter = 0;

        while (homeTeamList.size() > 0 && availableTeamCount > 0) {
            Pair<Team, Team> matchTeams = createMatch(awayPlayers, homeTeamList, awayTeamMap, createdMatchCounter);
            matchList.add(new Match(
                new MatchKey(matchTeams.getFirst().getId(), matchTeams.getSecond().getId(), round),
                matchTeams.getFirst(),
                matchTeams.getSecond()
            ));
            availableTeamCount--;
            createdMatchCounter++;
        }

        if (homeTeamList.size() > 0) {
            createMatchWithOnePlayer(round, homeTeamList, matchList);
        }
    }

    private List<Team> getHomeTeamList(HashMap<Player, List<Team>> awayTeamMap, Player homeTeamPlayer) {
        return awayTeamMap.remove(homeTeamPlayer);
    }

    private Pair<Team, Team> createMatch(
        List<Player> players,
        List<Team> homeTeamList,
        HashMap<Player, List<Team>> awayTeamMap,
        int createdMatchCounter
    ) {
        Team homeTeam = getHomeTeam(homeTeamList);
        Team awayTeam = getAwayTeam(players, awayTeamMap, createdMatchCounter);
        return Pair.of(homeTeam, awayTeam);
    }

    private void createMatchWithOnePlayer(Integer round, List<Team> teamList, List<Match> matchList) {
        while (!(teamList.size() <= 1)) {
            Team homeTeam = teamList.get(0);
            Team awayTeam = teamList.get(1);
            teamList.remove(1);
            teamList.remove(0);
            matchList.add(new Match(new MatchKey(homeTeam.getId(), awayTeam.getId(), round), homeTeam, awayTeam));
        }
    }

    private Team getAwayTeam(List<Player> players, HashMap<Player, List<Team>> awayTeamMap, int createdMatchCounter) {
        Player awayTeamPlayer = getAwayTeamPlayer(players, awayTeamMap, createdMatchCounter);
        List<Team> awayTeamList = awayTeamMap.get(awayTeamPlayer);
        Team awayTeam = awayTeamList.get(0);
        awayTeamList.remove(0);
        if (awayTeamList.size() > 0) {
            awayTeamMap.put(awayTeamPlayer, awayTeamList);
        } else {
            awayTeamMap.remove(awayTeamPlayer);
            players.remove(awayTeamPlayer);
        }
        return awayTeam;
    }

    private Player getAwayTeamPlayer(List<Player> players, HashMap<Player, List<Team>> awayTeamMap, int createdMatchCounter) {

        Player player = players.get(createdMatchCounter % players.size());
        while (awayTeamMap.get(player).size() <= 0) {
            awayTeamMap.remove(player);
            players.remove(player);
            player = players.get(createdMatchCounter % players.size());
        }
        return player;
    }

    private Team getHomeTeam(List<Team> homeTeamList) {
        Team homeTeam = homeTeamList.get(0);
        homeTeamList.remove(0);
        return homeTeam;
    }

    private int getAvailableTeamCount(HashMap<Player, List<Team>> awayTeamMap) {
        if (awayTeamMap.isEmpty()) {
            return 0;
        }
        return awayTeamMap.keySet().stream()
            .mapToInt(p -> awayTeamMap.get(p).size())
            .sum();
    }

    private Player getHomeTeamPlayer(HashMap<Player, List<Team>> teams, List<Player> awayPlayers) throws NoPlayerFoundException {
        Player homeTeamPlayer = teams.keySet().stream()
            .max(Comparator.comparing(p -> teams.get(p).size()))
            .orElseThrow(NoPlayerFoundException::new);
        awayPlayers.remove(homeTeamPlayer);
        return homeTeamPlayer;
    }

    private HashMap<Player, List<Team>> getNewTeamsInMap() {
        List<Player> players = playerService.getAllPlayers();
        HashMap<Player, List<Team>> awayTeamMap = new HashMap<>();
        players.forEach(p -> awayTeamMap.put(p, new ArrayList<>(p.getOwnTeams())));
        return awayTeamMap;
    }
    //endregion
}
