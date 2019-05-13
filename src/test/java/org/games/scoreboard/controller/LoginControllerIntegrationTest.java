package org.games.scoreboard.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.games.scoreboard.ScoreboardForGamesApplication;
import org.games.scoreboard.model.CredentialsRequestBody;
import org.junit.Assert;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreboardForGamesApplication.class})
@AutoConfigureMockMvc
public class LoginControllerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final MediaType MEDIA_TYPE_JSON = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostHttpRequesttoLogin_userExists_thenStatusAccepted() throws Exception {
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
        String content = result.getResponse().getContentAsString();
        Assert.assertNotNull(content);
    }

    @Test
    public void whenPostHttpRequesttoLogin_userNotExist_thenStatusUnauthorized() throws Exception {
        CredentialsRequestBody customer = new CredentialsRequestBody("coche", "password");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(customer);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MEDIA_TYPE_JSON)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void whenPostHttpRequesttoLogin_PasswordWrong_thenStatusUnauthorized() throws Exception {
        CredentialsRequestBody customer = new CredentialsRequestBody("admin", "password");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(customer);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MEDIA_TYPE_JSON)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}

