package org.games.scoreboard.security;

import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.games.scoreboard.security.UserPrincipal.DEFAULT_AUTHORITIES;


public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<UserDO> userOpt = userRepository.findOneByName(username);

        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        UserDO user = userOpt.get();
        return new UserPrincipal(user.getId(), user.getName(), user.getPassword(), DEFAULT_AUTHORITIES);

    }

}
