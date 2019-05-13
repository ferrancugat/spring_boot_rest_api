package org.games.scoreboard.repository;

import org.games.scoreboard.ScoreboardForGamesApplication;
import org.games.scoreboard.domain.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreboardForGamesApplication.class})
public class UserRepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUserRepository_whenSaveAndRetreiveEntity_thenOK() {

        UserDO genericEntity = userRepository.save(new UserDO("new_user", "password"));
        UserDO foundEntity = userRepository.findOneByName(genericEntity.getName()).orElse(null);

        assertNotNull(foundEntity);
        assertEquals(genericEntity.getName(), foundEntity.getName());
    }

}
