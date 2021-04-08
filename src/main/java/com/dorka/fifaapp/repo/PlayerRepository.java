package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findByName(String name);
}
