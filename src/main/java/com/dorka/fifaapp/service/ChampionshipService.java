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
    private DrawService drawService;
    private MatchRepository matchRepository;
    private ChampionshipDataRepository championshipDataRepository;

    private static Logger logger = Logger.getLogger("ChampionshipService");

    @Autowired
    public ChampionshipService(
            TeamService teamService, DrawService drawService,
            MatchRepository matchRepository, ChampionshipDataRepository championshipDataRepository) {
        this.teamService = teamService;
        this.drawService = drawService;
        this.matchRepository = matchRepository;
        this.championshipDataRepository = championshipDataRepository;
    }

    public void drawOfNewChampionship()
            throws InvalidNumberOfTeamsException, NoPlayerFoundException, NoChampionshipFoundException {
        drawService.checkIfPowerOfTwo(teamService.getAllTeams().size());
        ChampionshipData championshipData = startFirstRound();
        drawService.drawOfNewChampionship(championshipData);
    }

    public void drawOfOngoingChampionship()
            throws UnfinishedRoundException, NoChampionshipFoundException, NoPlayerFoundException {
        ChampionshipData championshipData = getNewRoundForDraw(getCurrentChampionshipData());
        drawService.createDraw(championshipData.getRound(), getWinnersOfRound(championshipData.getRound() - 1));
    }

    public void redrawCurrentRound() throws NoPlayerFoundException, NoChampionshipFoundException {
        ChampionshipData championshipData = getCurrentChampionshipData();
        deleteMatchesOfCurrentRound(championshipData);
        Integer currentRound = championshipData.getRound();
        if (currentRound > 1) {
            drawService.createDraw(championshipData.getRound(), getWinnersOfRound(championshipData.getRound() - 1));
        } else {
            drawService.drawOfNewChampionship(championshipData);
        }
    }

    public void setResultOfMatch(MatchResultDTO matchResult)
            throws InvalidTeamNameException, NoChampionshipFoundException, MissingParameterException, MatchResultException {
        checkMatchResultInput(matchResult);
        ChampionshipData championshipData = getCurrentChampionshipData();
        Match match = getMatch(matchResult, championshipData.getRound());
        match.setHomeTeamScore(matchResult.getHometeamScore());
        match.setAwayTeamScore(matchResult.getAwayteamScore());
        setWinnerOfMatch(match, matchResult);
        matchRepository.save(match);
        if (finishRoundIfNeeded(championshipData)) {
            finishChampionshipIfNeeded(championshipData);
        }
    }

    //region MatchResultPrivateMethods
    public String getWinnerOfLastChampionship() {
        try {
            return getCurrentChampionshipData().getWinnerName();
        } catch (NoChampionshipFoundException e) {
            return null;
        }
    }

    private void finishChampionshipIfNeeded(ChampionshipData championshipData) {
        HashMap<Player, List<Team>> winnersOfFinishedRound = getWinnersOfRound(championshipData.getRound());
        if (getAvailableTeamCount(winnersOfFinishedRound) == 1) {
            Player winningPlayer = winnersOfFinishedRound.keySet().stream()
                                        .findFirst().get();
            Team winningTeam = winnersOfFinishedRound.get(winningPlayer).get(0);
            championshipData.setWinnerName(winningTeam.getName() + " (" + winningPlayer.getName() + ")");
            championshipData.setOngoingChampionship(false);
            championshipDataRepository.save(championshipData);
        }
    }

    private boolean finishRoundIfNeeded(ChampionshipData championshipData) {
        if (checkIfRoundIsFinished(championshipData.getRound())) {
            championshipData.setOngoingRound(false);
            championshipDataRepository.save(championshipData);
            return true;
        }
        return false;
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
        if (matchResult.getHometeamScore().equals(matchResult.getAwayteamScore())
                || matchResult.getAwayteamScore() < 0 || matchResult.getHometeamScore() < 0) {
            throw new MatchResultException("The provided score is invalid");
        }
    }

    private void setWinnerOfMatch(Match match, MatchResultDTO matchResult) {
        if (matchResult.getAwayteamScore() < matchResult.getHometeamScore()) {
            match.setWinner(match.getHomeTeam());
        } else {
            match.setWinner(match.getAwayTeam());
        }
    }

    private Match getMatch(MatchResultDTO matchResult, Integer round)
            throws InvalidTeamNameException, MatchResultException {
        Team hometeam = teamService.getTeamByName(matchResult.getHometeamName());
        Team awayteam = teamService.getTeamByName(matchResult.getAwayteamName());
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

    public Boolean isOngoingTeamSelection() {
        Optional<ChampionshipData> optionalCSD = championshipDataRepository.findCurrentChampionshipData();
        if (optionalCSD.isPresent()) {
            return optionalCSD.get().isOngoingChampionship() && optionalCSD.get().isOngoingTeamSelection();
        }
        return false;
    }

    public void startNewChampionship() {
        finishPreviousChampionship();
        championshipDataRepository.save(new ChampionshipData());
    }

    public List<Match> getCurrentMatches() throws NoChampionshipFoundException {
        return matchRepository.findMatchesById_RoundNumber(getCurrentChampionshipData().getRound());
    }

    private void finishPreviousChampionship() {
        try {
            ChampionshipData lastChampionship = getCurrentChampionshipData();
            lastChampionship.setOngoingChampionship(false);
            lastChampionship.setOngoingRound(false);
            lastChampionship.setOngoingTeamSelection(false);
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
        championshipData.setOngoingTeamSelection(false);
        championshipData.setOngoingRound(true);
        championshipData.setRound(1);
        return championshipDataRepository.save(championshipData);
    }

    private HashMap<Player, List<Team>> getWinnersOfRound(Integer round) {
        List<Team> teams = matchRepository.findMatchesById_RoundNumber(round)
                .stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        HashMap<Player, List<Team>> playerAndTeamsMap = new HashMap<>();
        teams.forEach(team -> {
            playerAndTeamsMap.computeIfAbsent(team.getOwner(), k -> new ArrayList<>()).add(team);
        });
        return playerAndTeamsMap;
    }

    private int getAvailableTeamCount(HashMap<Player, List<Team>> teamMap) {
        if (teamMap.isEmpty()) {
            return 0;
        }
        return teamMap.keySet().stream()
                .mapToInt(p -> teamMap.get(p).size())
                .sum();
    }

    private void deleteMatchesOfCurrentRound(ChampionshipData championshipData) {
        matchRepository.deleteMatchesById_RoundNumber(championshipData.getRound());
    }

}
