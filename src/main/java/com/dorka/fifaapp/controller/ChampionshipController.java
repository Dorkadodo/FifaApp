package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.InvalidNumberOfTeamsException;
import com.dorka.fifaapp.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChampionshipController {

    private ChampionshipService championshipService;

    @Autowired
    public ChampionshipController(ChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    @GetMapping("/fifa/championship/draw")
    public String draw(Model model) throws InvalidNumberOfTeamsException {
        model.addAttribute("matchList", championshipService.draw());
        return "championshipPage";
    }
}
