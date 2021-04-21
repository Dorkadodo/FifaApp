package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.InvalidTeamNameException;
import com.dorka.fifaapp.exception.PlayerNameException;
import com.dorka.fifaapp.model.ChosenTeamListDTO;
import com.dorka.fifaapp.service.ChampionshipService;
import com.dorka.fifaapp.service.TeamService;
import com.dorka.fifaapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TeamController {

    private TeamService teamService;
    private PlayerService playerService;
    private ChampionshipService championshipService;

    @Autowired
    public TeamController(TeamService teamService, PlayerService playerService,
                          ChampionshipService championshipService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.championshipService = championshipService;
    }

    @GetMapping ("/fifa/team")
    public String showAllTeams(Model model) {
        model.addAttribute("teamList", teamService.getAvailableNationalTeams());
        model.addAttribute("chosenTeams", new ChosenTeamListDTO());
        model.addAttribute("playerList", playerService.getPlayersWithTeamCount());
        model.addAttribute("ongoingChampionship", championshipService.isOngoingChampionship());
        model.addAttribute("ongoingRound", championshipService.isOngoingRound());
        model.addAttribute("ongoingTeamSelection", championshipService.isOngoingTeamSelection());
        return "teamsPage";
    }

    @PostMapping("/fifa/team")
    public String something(@ModelAttribute ChosenTeamListDTO teams, @RequestParam String player) throws PlayerNameException {
        teamService.addTeamsToPlayer(teams.getTeams(), playerService.getPlayerByName(player));
        return "redirect:/fifa/team";
    }

    @PutMapping("/fifa/team/{id}")
    public String changeName (@PathVariable Long id, @RequestParam String name){
        teamService.changeName(id, name);
        return "redirect:/fifa/team";
    }

    @GetMapping("/fifa/team/delete/{teamName}")
    public String removeTeamFromPlayer (@PathVariable String teamName, @RequestParam String player)
            throws InvalidTeamNameException {
        teamService.deleteTeamByName(teamName);
        return "redirect:/fifa/player/" + player;
    }

}
