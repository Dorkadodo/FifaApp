package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.MyFileNotFoundException;
import com.dorka.fifaapp.model.Team;
import com.dorka.fifaapp.model.User;
import com.dorka.fifaapp.repo.TeamRepository;
import com.dorka.fifaapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MainService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    private List<String> teamNames;

    public List<String> getAvailableTeams() throws MyFileNotFoundException {
        return getAvailableNationalTeams();
    }

    public void changeName(Long id, String newName) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.setName(newName);
            teamRepository.save(team);
        }
    }

    public List<String> getAvailableNationalTeams() throws MyFileNotFoundException {
        List<String> alreadyChosenTeamList = ((List<Team>)teamRepository.findAll()).stream()
                .map(team -> team.getName())
                .collect(Collectors.toList());
        if (teamNames == null){
            return loadAllNationNames();
        }
        alreadyChosenTeamList.stream()
                .forEach(nation -> {
                    if (teamNames.contains(nation)) {
                        teamNames.remove(nation);
                    }
                });
        return teamNames;
    }

    public List<String> loadAllNationNames() throws MyFileNotFoundException {
        Path path = Paths.get("src/main/resources/static/nations.txt");
        teamNames = new ArrayList<>();
        try {
            teamNames = Files.readAllLines(path);
        } catch (IOException e) {
                throw new MyFileNotFoundException();
        }
        return teamNames;
    }

    public void chooseTeams(String[] teams, String userName){
        addTeamsToUser(teams, userName);
        removeChosenTeamsFromAvailableList(teams);
    }

    private void removeChosenTeamsFromAvailableList(String[] teams) {
        Arrays.stream(teams)
                .forEach(teamName -> {
                    if (teamNames.contains(teamName)) {
                        teamNames.remove(teamName);
                    }
                });
    }

    private void addTeamsToUser(String[] teams, String userName) {
        User user = userRepository.findByName(userName);
        if (user == null){
            user = userRepository.save(new User(userName));
        }
        for (String teamName: teams) {
            teamRepository.save(new Team(teamName, user));
        }
    }

}
