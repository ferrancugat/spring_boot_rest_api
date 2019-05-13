package org.games.scoreboard.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.games.scoreboard.security.SessionManager;
import org.games.scoreboard.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.games.scoreboard.security.UserPrincipal.DEFAULT_AUTHORITIES;

public class JwtSessionManager implements SessionManager {

    private static final String ID_CLAIM = "ID";
    private static final String USERNAME_CLAIM = "USERNAME";
    private final Logger logger = LoggerFactory.getLogger(JwtSessionManager.class);
    private String jwtSecretKeyBase64;

    private Integer jwtExpirationMs;

    private Key key;

    public JwtSessionManager(String jwtSecretKeyBase64, Integer jwtExpirationMs) {
        this.jwtSecretKeyBase64 = jwtSecretKeyBase64;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKeyBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateSessionKey(UserPrincipal userPrincipal) {
        Map<String, Object> claims = this.getClaims(userPrincipal);
        Date now = new Date();
        Date expiration = this.getExpirationDate(now);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    private Map<String, Object> getClaims(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_CLAIM, userPrincipal.getId());
        claims.put(USERNAME_CLAIM, userPrincipal.getUsername());
        return claims;

    }

    @Override
    public UserPrincipal getUserPrincipalFromSessionKey(String sessionId) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(sessionId)
                .getBody();

        return new UserPrincipal(claims.get(ID_CLAIM, Integer.class), claims.get(USERNAME_CLAIM, String.class),
                "", DEFAULT_AUTHORITIES);
    }


    @Override
    public boolean validateSessionKey(String token) {
        try {
            this.tryToParseToken(token);
            return true;
        } catch (Exception e) {
            logger.info("JWT token validation error.");
        }

        return false;
    }


    private void tryToParseToken(String token) {
        Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token);
    }

    private Date getExpirationDate(Date fromDate) {
        Date expirationDate = null;

        if (jwtExpirationMs != null) {
            expirationDate = new Date(fromDate.getTime() + jwtExpirationMs);
        }

        return expirationDate;
    }
}
