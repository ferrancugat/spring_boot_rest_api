package org.games.scoreboard.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

public class SessionKeyAuthorizationFilter extends OncePerRequestFilter {

    public static final String HTTP_HEADER_SESSION_ID = "Session-Key";

    private SessionManager sessionManager;

    public SessionKeyAuthorizationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        String sessionKey = this.getSessionKeyFromRequest(httpServletRequest);

        if (hasText(sessionKey) && sessionManager.validateSessionKey(sessionKey)) {
            UserPrincipal userPrincipal = sessionManager.getUserPrincipalFromSessionKey(sessionKey);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getSessionKeyFromRequest(HttpServletRequest httpServletRequest) {
        String sessionKey = httpServletRequest.getHeader(HTTP_HEADER_SESSION_ID);
        return sessionKey;
    }
}
