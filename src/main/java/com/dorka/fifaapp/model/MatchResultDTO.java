package com.dorka.fifaapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDTO {
    private String hometeamName;
    private String awayteamName;
    private Integer hometeamScore;
    private Integer awayteamScore;
}
