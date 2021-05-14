package com.dorka.fifaapp.exception.handler;

import com.dorka.fifaapp.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.apache.log4j.Logger;

@ControllerAdvice
public class MyExceptionHandler {

    private static Logger logger = Logger.getLogger("MyExceptionHandler");

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public String playerAlreadyExistsExceptionHandler() {
        return "redirect:/fifa/player?newNameError=true";
    }

    @ExceptionHandler(PlayerNameException.class)
    public String playerNameExceptionHandler(PlayerNameException ex, Model model) {
        logger.warn(ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidNumberOfTeamsException.class)
    public String invalidNumberOfTeamsExceptionHandler() {
        logger.warn("InvalidNumberOfTeamsException");
        return "redirect:/fifa/team?invalidNumberError=true";
    }

    @ExceptionHandler(UnfinishedRoundException.class)
    public String unfinishedRoundExceptionHandler(Model model) {
        logger.warn("UnfinishedRoundException");
        model.addAttribute("errorMessage", "Current round is not yet finished");
        return "error";
    }

    @ExceptionHandler(NoChampionshipFoundException.class)
    public String noChampionshipFoundExceptionHandler(Model model) {
        logger.warn("NoChampionshipFoundException");
        model.addAttribute("errorMessage", "Currently there is no ongoing championship");
        return "error";
    }

    @ExceptionHandler(NoPlayerFoundException.class)
    public String noPlayerFoundExceptionHandler(Model model) {
        logger.warn("NoPlayerFoundException");
        model.addAttribute("errorMessage", "Sorry, we couldn't find any player for the draw.");
        return "error";
    }

    @ExceptionHandler(MatchResultException.class)
    public String matchResultExceptionHandler(MatchResultException ex, Model model) {
        logger.warn(ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
