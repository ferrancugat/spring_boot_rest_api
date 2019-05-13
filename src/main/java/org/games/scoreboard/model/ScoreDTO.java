package org.games.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoreDTO {

    @JsonProperty("userid")
    private String username;
    @JsonProperty("score")
    private int value;

    public ScoreDTO() {

    }

    public ScoreDTO(String username, int value) {
        this.username = username;
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
