package org.games.scoreboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.games.scoreboard.**"})
public class ScoreboardForGamesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreboardForGamesApplication.class, args);
    }

}
