package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.InvalidNumberOfTeamsException;
import com.dorka.fifaapp.exception.NoChampionshipFoundException;
import com.dorka.fifaapp.exception.NoPlayerFoundException;
import com.dorka.fifaapp.exception.UnfinishedRoundException;
import com.dorka.fifaapp.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChampionshipController {

    private ChampionshipService championshipService;

    @Autowired
    public ChampionshipController(ChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    @GetMapping("/fifa/championship")
    public String championship(@RequestParam(required = false) String invalidNumberError, Model model) {
        if (invalidNumberError != null) {
            model.addAttribute("invalidNumberError", invalidNumberError);
        }
        model.addAttribute("ongoingChampionship", championshipService.isOngoingChampionship());
        model.addAttribute("ongoingRound", championshipService.isOngoingRound());
        return "championshipPage";
    }

    @GetMapping("/fifa/championship/new")
    public String newChampionship() {
        championshipService.startNewChampionship();
        return "redirect:/fifa/team";
    }

    @GetMapping("/fifa/championship/first-draw")
    public String firstDraw(Model model)
            throws InvalidNumberOfTeamsException, NoPlayerFoundException, NoChampionshipFoundException {
        model.addAttribute("matchList", championshipService.drawOfNewChampionship());
        model.addAttribute("ongoingChampionship", championshipService.isOngoingChampionship());
        model.addAttribute("ongoingRound", championshipService.isOngoingRound());
        return "championshipPage";
    }

    @GetMapping("/fifa/championship/ongoing-draw")
    public String ongoingDraw(Model model)
            throws UnfinishedRoundException, NoChampionshipFoundException, NoPlayerFoundException {
        model.addAttribute("matchList", championshipService.drawOfOngoingChampionship());
        model.addAttribute("ongoingChampionship", championshipService.isOngoingChampionship());
        model.addAttribute("ongoingRound", championshipService.isOngoingRound())
        return "championshipPage";
    }

//    @GetMapping("/fifa/championship/redraw")
//    public String redraw(Model model) throws InvalidNumberOfTeamsException, UnfinishedRoundException {
//        model.addAttribute("matchList", championshipService.redraw());
//        return "championshipPage";
//    }
}
