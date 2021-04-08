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
public class TeamService {

    private TeamRepository teamRepository;
    private UserRepository userRepository;
    private List<String> teamNames;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
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

    public List<String> getAvailableNationalTeams() throws MyFileNotFoundException {
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
        List<String> alreadyChosenTeamList = ((List<Team>)teamRepository.findAll()).stream()
                .map(team -> team.getName())
                .collect(Collectors.toList());
        return alreadyChosenTeamList;
    }

    private List<String> loadAllNationNames() throws MyFileNotFoundException {
        Path path = Paths.get("src/main/resources/static/nations.txt");
        teamNames = new ArrayList<>();
        try {
            teamNames = Files.readAllLines(path);
        } catch (IOException e) {
            throw new MyFileNotFoundException();
        }
        return teamNames;
    }

    public void changeName(Long id, String newName) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            team.setName(newName);
            teamRepository.save(team);
        }
    }



    public void chooseTeams(String[] teams, String userName){
        addTeamsToUser(teams, userName);
    }

    private void addTeamsToUser(String[] teams, String userName) {
        User user = userRepository.findByName(userName);
        if (user == null){
            user = userRepository.save(new User(userName));
        }
        for (String teamName: teams) {
            if (!teamName.equals("")) {
                teamRepository.save(new Team(teamName, user));
            }
        }
    }

}
