package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.PlayerAlreadyExistsException;
import com.dorka.fifaapp.exception.PlayerNameException;
import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.PlayerRequestDTO;
import com.dorka.fifaapp.model.PlayerTeamCountDTO;
import com.dorka.fifaapp.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void deletePlayer(String name) throws PlayerNameException {
        Player player = getPlayerByName(name);
        playerRepository.delete(player);
    }

    public void createPlayer(PlayerRequestDTO playerRequest) throws PlayerAlreadyExistsException {
        if (getAllPlayerName().contains(playerRequest.getNewName())) {
            throw new PlayerAlreadyExistsException();
        }
        playerRepository.save(new Player(playerRequest.getNewName()));
    }

    public Player getPlayerByName(String playerName) throws PlayerNameException {
        return playerRepository.findByName(playerName).orElseThrow(
                () -> new PlayerNameException("No Player found with this name!"));
    }

    public List<String> getAllPlayerName() {
        return ((List<Player>) playerRepository.findAll()).stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public List<PlayerTeamCountDTO> getPlayersWithTeamCount() {
        List<PlayerTeamCountDTO> playerTeamCountList = new ArrayList<>();
        playerRepository.findAll()
                .forEach(player -> playerTeamCountList.add(
                        new PlayerTeamCountDTO(player.getName(), player.getOwnTeams().size()))
                );
        return playerTeamCountList;
    }

}
