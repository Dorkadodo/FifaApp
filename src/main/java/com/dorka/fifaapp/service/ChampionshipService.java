package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.*;
import com.dorka.fifaapp.model.*;
import com.dorka.fifaapp.repo.ChampionshipDataRepository;
import com.dorka.fifaapp.repo.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ChampionshipService {

    private TeamService teamService;
    private PlayerService playerService;
    private MatchRepository matchRepository;
    private ChampionshipDataRepository championshipDataRepository;

    private static Logger logger = Logger.getLogger("ChampionshipService");

    @Autowired
    public ChampionshipService(
            TeamService teamService, PlayerService playerService,
            MatchRepository matchRepository, ChampionshipDataRepository championshipDataRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.matchRepository = matchRepository;
        this.championshipDataRepository = championshipDataRepository;
    }

    public List<Match> drawOfNewChampionship()
            throws InvalidNumberOfTeamsException, NoPlayerFoundException, NoChampionshipFoundException {
        List<Team> teams = teamService.getAllTeams();
        checkIfPowerOfTwo(teams.size());
        ChampionshipData championshipData = startFirstRound();
        Integer round = championshipData.getRound();
        createDraw(round, getNewTeamsInMap());
        return matchRepository.findMatchesById_RoundNumber(round);
    }

    public List<Match> drawOfOngoingChampionship()
            throws UnfinishedRoundException, NoChampionshipFoundException, NoPlayerFoundException {
        ChampionshipData championshipData = getCurrentChampionshipData();
        Integer round = getNewRoundForDraw(championshipData).getRound();
        HashMap<Player, List<Team>> teams = getWinnersOfLastRound(championshipData);
        createDraw(round, teams);
        return matchRepository.findMatchesById_RoundNumber(round);
    }


//    public List<Match> redraw() {
//    }

    public void setResultOfMatch(MatchResultDTO matchResult)
            throws InvalidTeamNameException, NoChampionshipFoundException, MissingParameterException, MatchResultException {
        checkMatchResultInput(matchResult);
        ChampionshipData championshipData = getCurrentChampionshipData();
        Match match = getMatch(matchResult, championshipData.getRound());
        match.setHomeTeamScore(matchResult.getHometeamScore());
        match.setAwayTeamScore(matchResult.getAwayteamScore());
        setWinner(match, matchResult);
        matchRepository.save(match);
        finishRoundIfNeeded(championshipData);
    }

    //region MatchResultPrivateMethods
    private void finishRoundIfNeeded(ChampionshipData championshipData) {
        if (checkIfRoundIsFinished(championshipData.getRound())) {
            championshipData.setOngoingRound(false);
            championshipDataRepository.save(championshipData);
        }
    }

    private Boolean checkIfRoundIsFinished(Integer round) {
        List<Match> currentMatchList = matchRepository.findMatchesById_RoundNumber(round);
        Optional<Match> noWinnerYet = currentMatchList.stream()
                .filter(match -> match.getWinner() == null)
                .findAny();
        return !noWinnerYet.isPresent();
    }

    private void checkMatchResultInput(MatchResultDTO matchResult) throws MissingParameterException, MatchResultException {
        if (matchResult == null || matchResult.getHometeamScore() == null || matchResult.getHometeamName() == null
                || matchResult.getAwayteamScore() == null || matchResult.getAwayteamName() == null) {
            throw new MissingParameterException();
        }
        if (matchResult.getAwayteamScore().equals(matchResult.getAwayteamScore())
                || matchResult.getAwayteamScore() < 0 || matchResult.getHometeamScore() < 0) {
            throw new MatchResultException("The provided score is invalid");
        }
    }

    private void setWinner(Match match, MatchResultDTO matchResult) {
        if (matchResult.getAwayteamScore() < matchResult.getHometeamScore()) {
            match.setWinner(match.getHomeTeam());
        } else {
            match.setWinner(match.getAwayTeam());
        }
    }

    private Match getMatch(MatchResultDTO matchResult, Integer round)
            throws InvalidTeamNameException, MatchResultException {
        Team hometeam = teamService.getByName(matchResult.getHometeamName());
        Team awayteam = teamService.getByName(matchResult.getAwayteamName());
        return matchRepository.findById_HomeTeamIdAndId_AwayTeamIdAndId_RoundNumber(
                hometeam.getId(), awayteam.getId(), round)
                .orElseThrow(() -> new MatchResultException("No match found with the given parameters!"));
    }
    //endregion

    public Boolean isOngoingChampionship() {
        Optional<ChampionshipData> optionalCSD = championshipDataRepository.findCurrentChampionshipData();
        if (optionalCSD.isPresent()) {
            return optionalCSD.get().isOngoingChampionship();
        }
        return false;
    }

    public Boolean isOngoingRound() {
        Optional<ChampionshipData> optionalCSD = championshipDataRepository.findCurrentChampionshipData();
        if (optionalCSD.isPresent()) {
            return optionalCSD.get().isOngoingChampionship() && optionalCSD.get().isOngoingRound();
        }
        return false;
    }

    public void startNewChampionship() {
        finishLastChampionship();
        championshipDataRepository.save(new ChampionshipData());
    }

    public List<Match> getCurrentMatches() throws NoChampionshipFoundException {
        return matchRepository.findMatchesById_RoundNumber(getCurrentChampionshipData().getRound());
    }

    private void finishLastChampionship() {
        try {
            ChampionshipData lastChampionship = getCurrentChampionshipData();
            lastChampionship.setOngoingChampionship(false);
            championshipDataRepository.save(lastChampionship);
        } catch (NoChampionshipFoundException e) {
            logger.log(Level.INFO, "didn't find previous championship");
        }
        matchRepository.deleteAll();
        teamService.deleteAllTeams();
    }

    private ChampionshipData getCurrentChampionshipData() throws NoChampionshipFoundException {
        Optional<ChampionshipData> optionalCSD = championshipDataRepository.findCurrentChampionshipData();
        return optionalCSD.orElseThrow(NoChampionshipFoundException::new);
    }

    //region DrawPrivateMethods
    private ChampionshipData getNewRoundForDraw(ChampionshipData championshipData) throws UnfinishedRoundException {
        if (championshipData.isOngoingRound()) {
            throw new UnfinishedRoundException();
        } else {
            Integer round = championshipData.getRound() + 1;
            championshipData.setRound(round);
            championshipData.setOngoingRound(true);
            return championshipDataRepository.save(championshipData);
        }
    }

    private ChampionshipData startFirstRound() throws NoChampionshipFoundException {
        ChampionshipData championshipData = getCurrentChampionshipData();
        championshipData.setOngoingRound(true);
        championshipData.setRound(1);
        return championshipDataRepository.save(championshipData);
    }

    private void createDraw(Integer round, HashMap<Player, List<Team>> awayTeamMap) throws NoPlayerFoundException {
        List<Player> players = new ArrayList<>(awayTeamMap.keySet());

        while (!awayTeamMap.isEmpty()) {
            assignTeamsOfOnePlayer(round, players, awayTeamMap);
        }
    }

    private void assignTeamsOfOnePlayer(Integer round, List<Player> awayPlayers, HashMap<Player, List<Team>> awayTeamMap) throws NoPlayerFoundException {
        Player homeTeamPlayer = getHomeTeamPlayer(awayTeamMap, awayPlayers);
        List<Team> homeTeamList = getHomeTeamList(awayTeamMap, homeTeamPlayer);
        int availableTeamCount = getAvailableTeamCount(awayTeamMap);
        int counter = 0;

        while (!(homeTeamList.size() <= 0 || availableTeamCount <= 0)) {
            createMatch(round, awayPlayers, homeTeamList, awayTeamMap, counter);
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
        while (!(teamList.size() <= 1)) {
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

    private Player getHomeTeamPlayer(HashMap<Player, List<Team>> teams, List<Player> awayPlayers) throws NoPlayerFoundException {
        Player homeTeamPlayer = teams.keySet().stream()
                .max(Comparator.comparing(p -> teams.get(p).size()))
                .orElseThrow(NoPlayerFoundException::new);
        awayPlayers.remove(homeTeamPlayer);
        return homeTeamPlayer;
    }

    private HashMap<Player, List<Team>> getWinnersOfLastRound(ChampionshipData championshipData) {
        List<Team> teams = matchRepository.findMatchesById_RoundNumber(championshipData.getRound() - 1)
                .stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        HashMap<Player, List<Team>> playerAndTeamsMap = new HashMap<>();
        teams.forEach(team -> {
            playerAndTeamsMap.computeIfAbsent(team.getOwner(), k -> new ArrayList<>()).add(team);
        });
        return playerAndTeamsMap;
    }

    private HashMap<Player, List<Team>> getNewTeamsInMap() {
        List<Player> players = playerService.getAllPlayers();
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
