package org.games.scoreboard.config;

import org.games.scoreboard.repository.ScoreRepository;
import org.games.scoreboard.service.ScoreService;
import org.games.scoreboard.service.ScoreServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {

    @Bean
    public ScoreService scoreService(ScoreRepository scoreRepository) {
        return new ScoreServiceImpl(scoreRepository);
    }
}
