package org.games.scoreboard.service;

import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.model.ScoreFilterType;
import org.games.scoreboard.repository.ScoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScoreServiceUnitTest {

    public static final int LEVEL = 1;
    public static final int SCORE = 10;

    @InjectMocks
    private ScoreServiceImpl scoreService;

    @Mock
    private ScoreRepository mockScoreRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSaveMethodCalled_then_EntityIsPersisted() {
        UserDO userDO = getMockUser();
        scoreService.saveScore(userDO, LEVEL, SCORE);
        verify(mockScoreRepository, times(1)).save(Mockito.any());
    }

    private UserDO getMockUser() {
        UserDO userDO = new UserDO();
        userDO.setId(1);
        userDO.setName("name");
        userDO.setPassword("password");
        return userDO;
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFetchScorePerLevel_WrongFiler_then_ExceptionThrown() {
        UserDO userDO = getMockUser();
        scoreService.getHighestScoringForLevel(LEVEL, ScoreFilterType.INVALID);
    }
}
