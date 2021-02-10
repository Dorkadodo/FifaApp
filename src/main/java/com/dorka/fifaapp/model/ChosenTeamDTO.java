package com.dorka.fifaapp.model;

public class ChosenTeamDTO {
    private String name;
    private boolean choosen;

    public ChosenTeamDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosen() {
        return choosen;
    }

    public void setChoosen(boolean choosen) {
        this.choosen = choosen;
    }
}
