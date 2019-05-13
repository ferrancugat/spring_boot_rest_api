package org.games.scoreboard.service;

import org.games.scoreboard.domain.ScoreDO;
import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.model.ScoreDTO;
import org.games.scoreboard.model.ScoreFilterType;
import org.games.scoreboard.repository.ScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void saveScore(UserDO user, int level, int result) {
        if (logger.isDebugEnabled()) {
            logger.debug("saving score for user: " + user.getName() + " for level: " + level + " with score:" + result);
        }
        ScoreDO score = new ScoreDO(level, result, user);
        scoreRepository.save(score);
    }

    @Override
    public List<ScoreDTO> getHighestScoringForLevel(int level, ScoreFilterType filter) {
        switch (filter) {
            case HIGHESTSCORE:
                return scoreRepository.findHighestScorePerUserByLevel(level);
            case NONE:
            case INVALID:
            default:
                logger.info("This scoring filter is not supported: " + filter);
                throw new IllegalArgumentException("This scoring filter is not supported:" + filter);
        }
    }
}
