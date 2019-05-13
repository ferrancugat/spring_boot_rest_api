package org.games.scoreboard.repository;

import org.games.scoreboard.domain.ScoreDO;
import org.games.scoreboard.model.ScoreDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("scoreRepository")
public interface ScoreRepository extends JpaRepository<ScoreDO, Integer> {


    @Query("select new org.games.scoreboard.model.ScoreDTO(sc.user.name as name, max (sc.value) as maxscore)from ScoreDO sc where sc.level= :level group by sc.user.name order by maxscore")
    List<ScoreDTO> findHighestScorePerUserByLevel(@Param("level") int level);

}
