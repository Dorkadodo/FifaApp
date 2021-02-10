package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
