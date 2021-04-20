package com.dorka.fifaapp.exception.handler;

import com.dorka.fifaapp.exception.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class MyExceptionHandler {

    private static Logger logger = Logger.getLogger("MyExceptionHandler");

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public String playerAlreadyExistsExceptionHandler() {
        return "redirect:/fifa/player?newNameError=true";
    }

    @ExceptionHandler(PlayerNameException.class)
    public String playerNameExceptionHandler(PlayerNameException ex, Model model) {
        logger.log(Level.WARNING, ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidNumberOfTeamsException.class)
    public String invalidNumberOfTeamsExceptionHandler() {
        logger.log(Level.WARNING, "InvalidNumberOfTeamsException");
        return "redirect:/fifa/championship?invalidNumberError=true";
    }

    @ExceptionHandler(UnfinishedRoundException.class)
    public String unfinishedRoundExceptionHandler(Model model) {
        logger.log(Level.WARNING, "UnfinishedRoundException");
        model.addAttribute("errorMessage", "Current round is not yet finished");
        return "error";
    }

    @ExceptionHandler(NoChampionshipFoundException.class)
    public String noChampionshipFoundExceptionHandler(Model model) {
        logger.log(Level.WARNING, "NoChampionshipFoundException");
        model.addAttribute("errorMessage", "Currently there is no ongoing championship");
        return "error";
    }

    @ExceptionHandler(NoPlayerFoundException.class)
    public String noPlayerFoundExceptionHandler(Model model) {
        logger.log(Level.WARNING, "NoPlayerFoundException");
        model.addAttribute("errorMessage", "Sorry, we couldn't find any player for the draw.");
        return "error";
    }
}
