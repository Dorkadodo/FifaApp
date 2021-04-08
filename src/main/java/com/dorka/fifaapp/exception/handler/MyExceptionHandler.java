package com.dorka.fifaapp.exception.handler;

import com.dorka.fifaapp.exception.PlayerAlreadyExistsException;
import com.dorka.fifaapp.exception.PlayerNameException;
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

}
