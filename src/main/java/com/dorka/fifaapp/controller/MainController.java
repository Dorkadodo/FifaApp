package com.dorka.fifaapp.controller;

import com.dorka.fifaapp.exception.MyFileNotFoundException;
import com.dorka.fifaapp.model.ChosenTeamDTO;
import com.dorka.fifaapp.model.ChosenTeamListDTO;
import com.dorka.fifaapp.model.Team;
import com.dorka.fifaapp.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.model.IModel;

import java.util.List;


@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping ("/")
    public String showAllTeams(Model model) throws MyFileNotFoundException {
        model.addAttribute("teamList", mainService.getAvailableTeams());
        model.addAttribute("choosenTeams", new ChosenTeamListDTO());
        return "index";
    }

    @GetMapping("/load-new-nations")
    public String loadNewNations() throws MyFileNotFoundException {
        mainService.loadAllNationNames();
        return "redirect:/";
    }

    @PostMapping("/choose-teams")
    public String something(@ModelAttribute ChosenTeamListDTO teams, @RequestParam String user, Model model){
        mainService.chooseTeams(teams.getTeams(), user);
        return "redirect:/";
    }

//    @GetMapping("/{id}")
//    public String changeName (@PathVariable Long id, @RequestParam String name){
//        mainService.changeName(id, name);
//        return "redirect:/";
//    }
}
