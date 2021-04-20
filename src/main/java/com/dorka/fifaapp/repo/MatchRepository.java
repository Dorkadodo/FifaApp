package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Match;
import com.dorka.fifaapp.model.MatchKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, MatchKey> {
    List<Match> findMatchesById_RoundNumber(Integer roundNumber);
}
