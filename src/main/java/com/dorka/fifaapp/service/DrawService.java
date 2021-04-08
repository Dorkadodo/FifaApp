package com.dorka.fifaapp.service;

import com.dorka.fifaapp.repo.TeamRepository;
import com.dorka.fifaapp.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DrawService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;



}
