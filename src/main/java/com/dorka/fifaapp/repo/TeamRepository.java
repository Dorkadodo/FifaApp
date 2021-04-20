package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {
    List<Team> findByOwner(Player player);

    Optional<Team> findByName(String name);
}
