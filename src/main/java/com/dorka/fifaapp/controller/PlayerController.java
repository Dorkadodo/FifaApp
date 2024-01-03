package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.PlayerAlreadyExistsException;
import com.dorka.fifaapp.exception.PlayerNameException;
import com.dorka.fifaapp.model.PlayerRequestDTO;
import com.dorka.fifaapp.service.ChampionshipService;
import com.dorka.fifaapp.service.TeamService;
import com.dorka.fifaapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final ChampionshipService championshipService;

    @Autowired
    public PlayerController(PlayerService playerService, TeamService teamService, ChampionshipService championshipService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.championshipService = championshipService;
    }

    @GetMapping("/fifa/player")
    public String playersPage(@RequestParam(required = false) Boolean newNameError, Model model) {
        if (newNameError != null) {
            model.addAttribute("newNameError", newNameError);
        }
        model.addAllAttributes(Map.of(
            "ongoingTeamSelection", championshipService.isOngoingTeamSelection(),
            "playerList", playerService.getAllPlayerNames(),
            "playerRequest", new PlayerRequestDTO()
        ));
        return "playersPage";
    }

    @PostMapping("/fifa/player/create")
    public String createPlayer(@ModelAttribute PlayerRequestDTO playerRequest) throws PlayerAlreadyExistsException {
        playerService.createPlayer(playerRequest);
        return "redirect:/fifa/player";
    }

    @GetMapping("/fifa/player/delete/{playerName}")
    public String deletePlayer(@PathVariable String playerName) throws PlayerNameException {
        playerService.deletePlayer(playerName);
        return "redirect:/fifa/player";
    }

    @GetMapping("/fifa/player/{playerName}")
    public String checkPlayer(@PathVariable String playerName, Model model) throws PlayerNameException {
        model.addAllAttributes(
            Map.of(
                "teamList", teamService.getTeamsByPlayer(playerService.getPlayerByName(playerName)),
                "ongoingTeamSelection", championshipService.isOngoingTeamSelection(),
                "playerName", playerName
            )
        );
        return "player";
    }

}
