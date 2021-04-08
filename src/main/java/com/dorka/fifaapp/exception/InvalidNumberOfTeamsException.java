package com.dorka.fifaapp.exception;

public class InvalidNumberOfTeamsException extends Exception {
    public InvalidNumberOfTeamsException(String message) {
        super(message);
    }

    public InvalidNumberOfTeamsException() {
        super();
    }
}
