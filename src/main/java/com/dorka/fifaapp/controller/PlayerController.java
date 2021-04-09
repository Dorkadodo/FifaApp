package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.PlayerAlreadyExistsException;
import com.dorka.fifaapp.exception.PlayerNameException;
import com.dorka.fifaapp.model.PlayerRequestDTO;
import com.dorka.fifaapp.service.TeamService;
import com.dorka.fifaapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PlayerController {

    private PlayerService playerService;
    private TeamService teamService;

    @Autowired
    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @GetMapping("/fifa/player")
    public String playersPage(@RequestParam(required = false) String newNameError, Model model) {
        model.addAttribute("playerRequest", new PlayerRequestDTO());
        if (newNameError != null) {
            model.addAttribute("newNameError", newNameError);
        }
        model.addAttribute("playerList", playerService.getAllPlayerNames());
        return "playersPage";
    }

    @PostMapping("/fifa/player/create")
    public String createPlayer(@ModelAttribute PlayerRequestDTO playerRequest) throws PlayerAlreadyExistsException {
        playerService.createPlayer(playerRequest);
        return "redirect:/fifa/player/";
    }

    @GetMapping("/fifa/player/delete/{playerName}")
    public String deletePlayer(@PathVariable String playerName) throws PlayerNameException {
        playerService.deletePlayer(playerName);
        return "redirect:/fifa/player";
    }

    @GetMapping("/fifa/player/{playerName}")
    public String checkPlayer(@PathVariable String playerName, Model model) throws PlayerNameException {
        model.addAttribute("teamList",
                teamService.getTeamsByPlayer(playerService.getPlayerByName(playerName)));
        model.addAttribute("playerName", playerName);
        return "player";
    }

}
