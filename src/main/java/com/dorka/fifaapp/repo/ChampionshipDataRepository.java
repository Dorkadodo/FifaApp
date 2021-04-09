package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.ChampionshipData;
import com.dorka.fifaapp.model.ChampionshipDataType;
import org.springframework.data.repository.CrudRepository;

public interface ChampionshipDataRepository extends CrudRepository<ChampionshipData, ChampionshipDataType> {
}
