package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.InvalidTeamNameException;
import com.dorka.fifaapp.exception.MyFileNotFoundException;
import com.dorka.fifaapp.model.Team;
import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private List<String> teamNames;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        try {
            teamNames = loadAllNationNames();
        } catch (MyFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTeamNames() throws MyFileNotFoundException {
        if (teamNames.isEmpty()) {
            throw new MyFileNotFoundException();
        }
        return teamNames;
    }

    public List<String> getAvailableNationalTeams() {
        List<String> alreadyChosenTeamList = getAlreadyChosenTeamList();
        return getAvailableTeamNames(alreadyChosenTeamList);
    }

    private List<String> getAvailableTeamNames(List<String> alreadyChosenTeamList) {
        List<String> availableTeamNames = new ArrayList<>();
        teamNames.forEach(teamName -> {
            if (!alreadyChosenTeamList.contains(teamName)) {
                availableTeamNames.add(teamName);
            }
        });
        return availableTeamNames;
    }

    private List<String> getAlreadyChosenTeamList() {
        List<String> alreadyChosenTeamList = ((List<Team>) teamRepository.findAll()).stream()
                .map(team -> team.getName())
                .collect(Collectors.toList());
        return alreadyChosenTeamList;
    }

    private List<String> loadAllNationNames() throws MyFileNotFoundException {
        teamNames = new ArrayList<>();
        InputStream is = getFileFromResourceAsStream("nations.txt");
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                teamNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teamNames;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public void changeName(Long id, String newName) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.setName(newName);
            teamRepository.save(team);
        }
    }

    public void addTeamsToPlayer(String[] teams, Player player) {
        for (String teamName : teams) {
            if (!teamName.equals("")) {
                teamRepository.save(new Team(teamName, player));
            }
        }
    }

    public List<String> getTeamsByPlayer(Player player) {
        return teamRepository.findByOwner(player).stream()
                .map(Team::getName)
                .collect(Collectors.toList());
    }

    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    public void deleteAllTeams() {
        teamRepository.deleteAll();
    }

    public Team getTeamByName(String name) throws InvalidTeamNameException {
        return teamRepository.findByName(name).orElseThrow(InvalidTeamNameException::new);
    }

    public void deleteTeamByName(String teamName) throws InvalidTeamNameException {
        Team team = getTeamByName(teamName);
        teamRepository.delete(team);
    }
}
