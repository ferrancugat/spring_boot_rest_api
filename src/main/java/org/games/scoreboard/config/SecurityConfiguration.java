package org.games.scoreboard.config;

import org.games.scoreboard.repository.UserRepository;
import org.games.scoreboard.security.Http401UnauthorizedEntryPoint;
import org.games.scoreboard.security.SessionKeyAuthorizationFilter;
import org.games.scoreboard.security.SessionManager;
import org.games.scoreboard.security.UserDetailsServiceImpl;
import org.games.scoreboard.security.jwt.JwtSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final int _10_MINUTES = 6000000;
    @Resource
    public Environment env;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    SessionManager sessionManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(http401UnauthorizedEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**/login").permitAll()
                .antMatchers(HttpMethod.GET, "/**/level/*/score").permitAll()
                .anyRequest().authenticated()
                //another option with JdbcTokenRepositoryImpl
                //.tokenRepository(persistentTokenRepository()).tokenValiditySeconds(86400)
                .and()
                .addFilterBefore(new SessionKeyAuthorizationFilter(sessionManager), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        //this is for demo purposes..password saved as plain text
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authProvider;
    }

    @Bean
    UserDetailsService userDetailsServiceImpl(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }

    @Bean
    public SessionManager sessionManager() {
        return new JwtSessionManager(env.getProperty("scoreboard.security.jwt-secret-key-base64"),
                env.getProperty("scoreboard.security.jwt-expiration-ms", Integer.class, _10_MINUTES));
    }
}
