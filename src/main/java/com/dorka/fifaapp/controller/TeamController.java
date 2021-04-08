package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.MyFileNotFoundException;
import com.dorka.fifaapp.model.ChosenTeamListDTO;
import com.dorka.fifaapp.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TeamController {

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping ("/")
    public String showAllTeams(Model model) throws MyFileNotFoundException {
        model.addAttribute("teamList", teamService.getAvailableNationalTeams());
        model.addAttribute("chosenTeams", new ChosenTeamListDTO());
        return "index";
    }

//    @GetMapping("/load-new-nations")
//    public String loadNewNations() throws MyFileNotFoundException {
//        teamService.loadAllNationNames();
//        return "redirect:/";
//    }

    @PostMapping("/choose-teams")
    public String something(@ModelAttribute ChosenTeamListDTO teams, @RequestParam String user){
        teamService.chooseTeams(teams.getTeams(), user);
        return "redirect:/";
    }

//    @GetMapping("/{id}")
//    public String changeName (@PathVariable Long id, @RequestParam String name){
//        mainService.changeName(id, name);
//        return "redirect:/";
//    }
}
