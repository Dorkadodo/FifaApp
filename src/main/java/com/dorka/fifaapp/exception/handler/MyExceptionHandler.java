package com.dorka.fifaapp.exception.handler;

import com.dorka.fifaapp.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger logger = LogManager.getLogger("MyExceptionHandler");

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public String playerAlreadyExistsExceptionHandler(PlayerAlreadyExistsException ex) {
        logger.warn(PlayerAlreadyExistsException.class.getSimpleName());
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
        logger.warn(InvalidNumberOfTeamsException.class.getSimpleName());
        return "redirect:/fifa/team?invalidNumberError=true";
    }

    @ExceptionHandler(UnfinishedRoundException.class)
    public String unfinishedRoundExceptionHandler(Model model) {
        logger.warn(UnfinishedRoundException.class.getSimpleName());
        model.addAttribute("errorMessage", "Current round is not yet finished");
        return "error";
    }

    @ExceptionHandler(NoChampionshipFoundException.class)
    public String noChampionshipFoundExceptionHandler(Model model) {
        logger.warn(NoChampionshipFoundException.class.getSimpleName());
        model.addAttribute("errorMessage", "Currently there is no ongoing championship");
        return "error";
    }

    @ExceptionHandler(NoPlayerFoundException.class)
    public String noPlayerFoundExceptionHandler(Model model) {
        logger.warn(NoPlayerFoundException.class.getSimpleName());
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