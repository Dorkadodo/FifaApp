package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.Match;
import com.dorka.fifaapp.model.MatchKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends CrudRepository<Match, MatchKey> {
    List<Match> findMatchesById_RoundNumber(Integer roundNumber);

    @Transactional
    void deleteMatchesById_RoundNumber(Integer roundNumber);

    Optional<Match> findById_HomeTeamIdAndId_AwayTeamIdAndId_RoundNumber(
            Long homeTeamId, Long awayTeamId, Integer roundNumber);
}
