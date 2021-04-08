package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Player;
import com.dorka.fifaapp.model.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {
    List<Team> findByOwner(Player player);
}
