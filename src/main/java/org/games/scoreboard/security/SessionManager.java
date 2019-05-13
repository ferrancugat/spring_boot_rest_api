package org.games.scoreboard.security;

public interface SessionManager {

    String generateSessionKey(UserPrincipal userPrincipal);

    UserPrincipal getUserPrincipalFromSessionKey(String sessionId);

    boolean validateSessionKey(String token);
}
