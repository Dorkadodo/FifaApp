package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.ChampionshipData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChampionshipDataRepository extends CrudRepository<ChampionshipData, Long> {
    @Query(value = "SELECT * FROM CHAMPIONSHIP_DATA ORDER BY START_TIME DESC LIMIT 1", nativeQuery = true)
    Optional<ChampionshipData> findCurrentChampionshipData();
}
