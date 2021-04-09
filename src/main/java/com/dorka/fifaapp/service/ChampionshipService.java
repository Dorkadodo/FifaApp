package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.InvalidNumberOfTeamsException;
import com.dorka.fifaapp.model.*;
import com.dorka.fifaapp.repo.ChampionshipDataRepository;
import com.dorka.fifaapp.repo.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChampionshipService {

    private TeamService teamService;
    private PlayerService playerService;
    private MatchRepository matchRepository;
    private ChampionshipDataRepository championshipDataRepository;

    @Autowired
    public ChampionshipService(
            TeamService teamService, PlayerService playerService,
            MatchRepository matchRepository, ChampionshipDataRepository championshipDataRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.matchRepository = matchRepository;
        this.championshipDataRepository = championshipDataRepository;
    }

    public List<Match> draw() throws InvalidNumberOfTeamsException {
        List<Team> teams = teamService.getAllTeams();
        checkIfPowerOfTwo(teams.size());
        Integer round = getNewRoundForDraw();
        createDraw(round);
        return matchRepository.findMatchesById_RoundNumber(round);
    }

    //region DrawPrivateMethods
    private Integer getNewRoundForDraw() {
        Optional<ChampionshipData> optionalRound = championshipDataRepository.findById(ChampionshipDataType.ROUND);
        if (optionalRound.isPresent()) {
            return getRoundPlusOne(optionalRound.get());
        } else {
            return getFirstRound();
        }
    }

    private Integer getFirstRound() {
        Integer round = 1;
        championshipDataRepository.save(new ChampionshipData(ChampionshipDataType.ROUND, round.toString()));
        return round;
    }

    private Integer getRoundPlusOne(ChampionshipData roundData) {
        Integer round = Integer.parseInt(roundData.getValue()) + 1;
        roundData.setValue(round.toString());
        championshipDataRepository.save(roundData);
        return round;
    }

    private void createDraw(Integer round) {
        List<Player> players = playerService.getAllPlayers();
        HashMap<Player, List<Team>> awayTeamMap = getTeamsInMap(players);

        while (!awayTeamMap.isEmpty()) {
            assignTeamsOfOnePlayer(round, players, awayTeamMap);
        }
    }

    private void assignTeamsOfOnePlayer(Integer round, List<Player> players, HashMap<Player, List<Team>> awayTeamMap) {
        Player homeTeamPlayer = getHomeTeamPlayer(players);
        List<Team> homeTeamList = getHomeTeamList(awayTeamMap, homeTeamPlayer);
        int availableTeamCount = getAvailableTeamCount(awayTeamMap);
        int counter = 0;

        while (!(homeTeamList.size() <= 0 || availableTeamCount <= 0)) {
            createMatch(round, players, homeTeamList, awayTeamMap, counter);
            availableTeamCount--;
            counter++;
        }
        if (homeTeamList.size() <= 0) {
            return;
        }
        createMatchWithOnePlayer(round, homeTeamList);
    }

    private List<Team> getHomeTeamList(HashMap<Player, List<Team>> awayTeamMap, Player homeTeamPlayer) {
        List<Team> homeTeamList = new ArrayList<>(awayTeamMap.get(homeTeamPlayer));
        awayTeamMap.remove(homeTeamPlayer);
        return homeTeamList;
    }

    private void createMatch(Integer round, List<Player> players, List<Team> homeTeamList, HashMap<Player, List<Team>> awayTeamMap, int counter) {
        Team homeTeam = getHomeTeam(homeTeamList);
        Team awayTeam = getAwayTeam(players, awayTeamMap, counter);
        matchRepository.save(new Match(new MatchKey(homeTeam.getId(), awayTeam.getId(), round), homeTeam, awayTeam));
    }

    private void createMatchWithOnePlayer(Integer round, List<Team> teamList) {
        while (!(teamList.size() <= 0)) {
            Team homeTeam = teamList.get(0);
            Team awayTeam = teamList.get(1);
            teamList.remove(1);
            teamList.remove(0);
            matchRepository.save(new Match(new MatchKey(homeTeam.getId(), awayTeam.getId(), round), homeTeam, awayTeam));
        }
    }

    private Team getAwayTeam(List<Player> players, HashMap<Player, List<Team>> awayTeamMap, int counter) {
        Player awayTeamPlayer = getAwayTeamPlayer(players, awayTeamMap, counter);
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

    private Player getAwayTeamPlayer(List<Player> players, HashMap<Player, List<Team>> awayTeamMap, int counter) {
        Player player = players.get(counter % players.size());
        while (!(awayTeamMap.get(player).size() > 0)) {
            awayTeamMap.remove(player);
            players.remove(player);
            player = players.get(counter % players.size());
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

    private Player getHomeTeamPlayer(List<Player> players) {
        Player homeTeamPlayer = players.stream()
                .max(Comparator.comparing(p -> p.getOwnTeams().size())).get();
        players.remove(homeTeamPlayer);
        return homeTeamPlayer;
    }

    private HashMap<Player, List<Team>> getTeamsInMap(List<Player> players) {
        HashMap<Player, List<Team>> awayTeamMap = new HashMap<>();
        players.forEach(p -> awayTeamMap.put(p, new ArrayList<>(p.getOwnTeams())));
        return awayTeamMap;
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
    //endregion
}
