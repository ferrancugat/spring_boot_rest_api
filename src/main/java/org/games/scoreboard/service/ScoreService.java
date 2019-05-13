package org.games.scoreboard.service;

import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.model.ScoreDTO;
import org.games.scoreboard.model.ScoreFilterType;

import java.util.List;

public interface ScoreService {

    void saveScore(UserDO user, int level, int result);

    List<ScoreDTO> getHighestScoringForLevel(int level, ScoreFilterType filter);
}
