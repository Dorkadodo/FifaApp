package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.*;
import com.dorka.fifaapp.model.MatchResultDTO;
import com.dorka.fifaapp.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ChampionshipController {

    private final ChampionshipService championshipService;
    private static final Logger logger = Logger.getLogger("ChampionshipController");

    @Autowired
    public ChampionshipController(ChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    @GetMapping("/")
    public String root(){
        return "redirect:/fifa/championship";
    }

    @GetMapping("/fifa/championship")
    public String championship(Model model) {
        model.addAllAttributes(generalAttributes());
        return "championshipPage";
    }

    @GetMapping("/fifa/championship/new")
    public String newChampionship() {
        championshipService.startNewChampionship();
        return "redirect:/fifa/team";
    }

    @GetMapping("/fifa/championship/first-draw")
    public String firstDraw()
            throws InvalidNumberOfTeamsException, NoPlayerFoundException, NoChampionshipFoundException {
        championshipService.drawOfNewChampionship();
        return "redirect:/fifa/championship";
    }

    @GetMapping("/fifa/championship/ongoing-draw")
    public String ongoingDraw()
            throws UnfinishedRoundException, NoChampionshipFoundException, NoPlayerFoundException {
        championshipService.drawOfOngoingChampionship();
        return "redirect:/fifa/championship";
    }

    @GetMapping("/fifa/championship/redraw")
    public String redraw(Model model) throws NoPlayerFoundException, NoChampionshipFoundException {
        championshipService.redrawCurrentRound();
        return "redirect:/fifa/championship";
    }

    @PostMapping("/fifa/championship/match")
    public String setResult(@ModelAttribute MatchResultDTO matchResult)
            throws InvalidTeamNameException, NoChampionshipFoundException,
            MissingParameterException, MatchResultException {
        championshipService.setResultOfMatch(matchResult);
        return "redirect:/fifa/championship";
    }

    private HashMap<String, Object> generalAttributes() {
        HashMap<String, Object> generalAttributes = new HashMap<>();
        try {
            generalAttributes.put("matchList", championshipService.getCurrentMatches());
        } catch (NoChampionshipFoundException e) {
            logger.log(Level.WARNING, "NoChampionshipFoundException");
        }
        generalAttributes.put("matchResult", new MatchResultDTO());
        generalAttributes.put("ongoingChampionship", championshipService.isOngoingChampionship());
        generalAttributes.put("ongoingRound", championshipService.isOngoingRound());
        generalAttributes.put("ongoingTeamSelection", championshipService.isOngoingTeamSelection());
        generalAttributes.put("winnerOfLastChampionship", championshipService.getWinnerOfLastChampionship());
        return generalAttributes;
    }
}
