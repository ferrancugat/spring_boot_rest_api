package org.games.scoreboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Credentials to authenticate a user.
 */
public class CredentialsRequestBody {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public CredentialsRequestBody() {

    }

    public CredentialsRequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Unique identifier of the user.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Unique identifier of the user.
     *
     * @return username
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}


