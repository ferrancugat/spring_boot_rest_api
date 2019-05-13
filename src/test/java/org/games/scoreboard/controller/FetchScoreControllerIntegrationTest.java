package org.games.scoreboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.games.scoreboard.ScoreboardForGamesApplication;
import org.games.scoreboard.domain.ScoreDO;
import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.model.ScoreDTO;
import org.games.scoreboard.repository.ScoreRepository;
import org.games.scoreboard.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreboardForGamesApplication.class})
@AutoConfigureMockMvc
public class FetchScoreControllerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static final int _1_LEVEL = 1;
    public static final int _100_SCORE = 100;
    public static final int _500_SCORE = 500;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void whenGetHttpRequesttoLevelScore_thenStatusOK_and_highestscore_returned() throws Exception {
        saveSomeScoring();

        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/level/" + _1_LEVEL + "/score?filter=highestscore"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ScoreDTO[] scores = objectMapper.readValue(response, ScoreDTO[].class);
        Assert.assertNotNull(scores);
        Assert.assertTrue(scores.length == 1);
        Assert.assertTrue(scores[0].getValue() == _500_SCORE);
    }


    private void saveSomeScoring() {
        //Fetch one user
        UserDO user = userRepository.getOne(1);
        //save several scores
        scoreRepository.save(new ScoreDO(_1_LEVEL, _100_SCORE, user));
        scoreRepository.save(new ScoreDO(_1_LEVEL, _100_SCORE, user));
        scoreRepository.save(new ScoreDO(_1_LEVEL, _100_SCORE, user));
        scoreRepository.save(new ScoreDO(_1_LEVEL, _500_SCORE, user));
        scoreRepository.save(new ScoreDO(_1_LEVEL, _500_SCORE, user));
    }

    @Test
    public void whenGetHttpRequesttoLevelScore_wrongfilter_thenStatusBadRequest() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/level/" + _1_LEVEL + "/score?filter=lowestscore"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenWrongMethodHttpRequesttoLevelScore_thenStatusUnauthorized() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/level/" + _1_LEVEL + "/score?filter=lowestscore"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
