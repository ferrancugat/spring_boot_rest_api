package org.games.scoreboard.security;

import org.games.scoreboard.security.jwt.JwtSessionManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class SessionManagerUnitTest {

    public static final int _10_MINUTES_EXPIRATION_MS = 600000;
    public static final int _1_SECOND_EXPIRATION_MS = 1000;
    public static final int _2_SECOND_EXPIRATION_MS = 2000;
    final String BASE64_SECRETKEY_JWT = "VGhpcyBzZWNyZXQga2V5IG11c3QgYmUgZW5jb2RlZCB1c2luZyBCYXNlNjQgYW5kIGF0IGxlYXN0IDY0IGJ5dGVzIGxvbmcu";


    @Test
    public void whenGenerateSessionKey_and_validateOK_and_userDecodeOK() {
        SessionManager sessionManager = new JwtSessionManager(BASE64_SECRETKEY_JWT, _10_MINUTES_EXPIRATION_MS);
        ((JwtSessionManager) sessionManager).init();
        UserPrincipal userPrincipal = new UserPrincipal(1, "username", "password", Collections.emptyList());
        String sessionKey = sessionManager.generateSessionKey(userPrincipal);
        Assert.assertNotNull(sessionKey);
        boolean isValid = sessionManager.validateSessionKey(sessionKey);
        Assert.assertTrue(isValid);
        UserPrincipal userFromSession = sessionManager.getUserPrincipalFromSessionKey(sessionKey);
        Assert.assertEquals(userPrincipal.getId(), userFromSession.getId());
        Assert.assertEquals(userFromSession.getUsername(), userPrincipal.getUsername());
    }

    @Test
    public void whenGenerateSessionKey_with_1SecondExpiration__validateWRONG_after_2Seconds() throws InterruptedException {
        SessionManager sessionManager = new JwtSessionManager(BASE64_SECRETKEY_JWT, _1_SECOND_EXPIRATION_MS);
        ((JwtSessionManager) sessionManager).init();
        UserPrincipal userPrincipal = new UserPrincipal(1, "username", "password", Collections.emptyList());
        String sessionKey = sessionManager.generateSessionKey(userPrincipal);
        Assert.assertNotNull(sessionKey);
        Thread.sleep(_2_SECOND_EXPIRATION_MS);
        boolean isValid = sessionManager.validateSessionKey(sessionKey);
        Assert.assertFalse(isValid);
    }
}
