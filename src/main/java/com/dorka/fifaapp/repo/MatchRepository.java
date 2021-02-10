package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Match;
import com.dorka.fifaapp.model.MatchKey;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, MatchKey> {
}
