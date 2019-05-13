package org.games.scoreboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.games.scoreboard.ScoreboardForGamesApplication;
import org.games.scoreboard.model.CredentialsRequestBody;
import org.games.scoreboard.security.SessionKeyAuthorizationFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreboardForGamesApplication.class})
@AutoConfigureMockMvc
public class SaveScoreControllerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final static MediaType MEDIA_TYPE_JSON = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    String sessionKey;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        if (sessionKey == null) {
            sessionKey = generateUserSessionKey();
        }
    }

    @Test
    public void whenSessionKey_saveScore_then_nocontent() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/level/3/score/1500")
                        .header(SessionKeyAuthorizationFilter.HTTP_HEADER_SESSION_ID, sessionKey)
                )

                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void whenSessionKey_saveScore_wrong_Method_then_methodnotallowed() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/level/3/score/1500")
                        .header(SessionKeyAuthorizationFilter.HTTP_HEADER_SESSION_ID, sessionKey)
                )

                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());

    }

    @Test
    public void when_wrongSessionKey_saveScore_then_unautharized() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/level/3/score/1500")
                        .header(SessionKeyAuthorizationFilter.HTTP_HEADER_SESSION_ID, "wrong")
                )

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }


    private String generateUserSessionKey() throws Exception {
        CredentialsRequestBody customer = new CredentialsRequestBody("admin", "admin");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(customer);

        MvcResult result =
                this.mockMvc
                        .perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MEDIA_TYPE_JSON)
                                .content(requestJson)
                        )

                        .andExpect(MockMvcResultMatchers.status().isAccepted())
                        .andReturn();
        String sessionKey = result.getResponse().getContentAsString();
        return sessionKey;
    }
}
