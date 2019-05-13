package org.games.scoreboard.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

public class UserPrincipal extends User {

    private static final long serialVersionUID = 248909203422807069L;
    public static Collection<SimpleGrantedAuthority> DEFAULT_AUTHORITIES = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

    private Integer id;

    public UserPrincipal(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
