package org.games.scoreboard.repository;

import io.jsonwebtoken.lang.Collections;
import org.games.scoreboard.ScoreboardForGamesApplication;
import org.games.scoreboard.domain.ScoreDO;
import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.model.ScoreDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreboardForGamesApplication.class})
public class ScoreRepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static final int USER_1_ID = 1;
    public static final int GAME_LEVEL = 1;
    public static final int USER_2_ID = 2;
    public static final int WRONG_LEVEL = 6;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDO userTest_1;
    private UserDO userTest_2;

    @Before
    public void setup() {
        userTest_1 = userRepository.getOne(USER_1_ID);
        assertNotNull(userTest_1);
        userTest_2 = userRepository.getOne(USER_2_ID);
        assertNotNull(userTest_2);
    }

    @Test
    public void givenUserRepository_whenSaveAndRetreiveEntity_thenOK() {

        ScoreDO genericEntity = scoreRepository.save(new ScoreDO(GAME_LEVEL, 10000, userTest_1));
        ScoreDO foundEntity = scoreRepository.getOne(genericEntity.getId());

        assertNotNull(foundEntity);
        assertEquals(genericEntity.getValue(), foundEntity.getValue());
        assertEquals(genericEntity.getLevel(), foundEntity.getLevel());
    }

    @Test
    public void givenSeveralRecordsSameLevelDifferentScoreSameUser_whenRetreiveHighest_then_1record_OK() {

        scoreRepository.save(new ScoreDO(GAME_LEVEL, 100, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 1000, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 10000, userTest_1));
        List<ScoreDTO> highestScorings = scoreRepository.findHighestScorePerUserByLevel(GAME_LEVEL);

        assertNotNull(highestScorings);
        assertTrue(highestScorings.size() == 1);
        assertEquals(highestScorings.get(0).getValue(), 10000);
    }

    @Test
    public void givenSeveralRecordsSameLevelDifferentScoreDiiferentUser_whenRetreiveHighest_then_2record_OK() {

        //user 1
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 100, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 1000, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 10000, userTest_1));

        //user 2
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 300, userTest_2));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 3000, userTest_2));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 30000, userTest_2));


        List<ScoreDTO> highestScorings = scoreRepository.findHighestScorePerUserByLevel(GAME_LEVEL);


        assertNotNull(highestScorings);
        assertTrue(highestScorings.size() == 2);
        if (highestScorings.get(0).getUsername().equals(userTest_1.getName())) {
            assertEquals(highestScorings.get(0).getValue(), 10000);
            assertEquals(highestScorings.get(1).getValue(), 30000);
        } else {
            assertEquals(highestScorings.get(0).getValue(), 10000);
            assertEquals(highestScorings.get(1).getValue(), 30000);
        }

    }

    @Test
    public void givenSeveralRecordsSameLevel_askForAnotherLevel_whenRetreive_then_0record_OK() {

        //user 1
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 100, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 1000, userTest_1));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 10000, userTest_1));

        //user 2
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 300, userTest_2));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 3000, userTest_2));
        scoreRepository.save(new ScoreDO(GAME_LEVEL, 30000, userTest_2));

        List<ScoreDTO> highestScorings = scoreRepository.findHighestScorePerUserByLevel(WRONG_LEVEL);

        assertTrue(Collections.isEmpty(highestScorings));

    }
}
