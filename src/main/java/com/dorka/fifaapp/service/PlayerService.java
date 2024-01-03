package com.dorka.fifaapp.service;

import com.dorka.fifaapp.exception.PlayerAlreadyExistsException;
import com.dorka.fifaapp.exception.PlayerNameException;
import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.PlayerRequestDTO;
import com.dorka.fifaapp.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void deletePlayer(String name) throws PlayerNameException {
        Player player = getPlayerByName(name);
        playerRepository.delete(player);
    }

    public void createPlayer(PlayerRequestDTO playerRequest) throws PlayerAlreadyExistsException {
        if (getAllPlayerNames().contains(playerRequest.getNewName())) {
            throw new PlayerAlreadyExistsException();
        }
        playerRepository.save(new Player(playerRequest.getNewName()));
    }

    public Player getPlayerByName(String playerName) throws PlayerNameException {
        return playerRepository.findByName(playerName).orElseThrow(
                () -> new PlayerNameException("No Player found with this name!"));
    }

    public List<String> getAllPlayerNames() {
        return ((List<Player>) playerRepository.findAll()).stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public HashMap<String, Integer> getPlayersWithTeamCount() {
        HashMap<String, Integer> playerTeamCountMap = new HashMap<>();
        playerRepository.findAll()
                .forEach(player -> playerTeamCountMap.put(player.getName(), player.getOwnTeams().size()));
        return playerTeamCountMap;
    }

    public List<Player> getAllPlayers() {
        return (List<Player>)playerRepository.findAll();
    }

}
