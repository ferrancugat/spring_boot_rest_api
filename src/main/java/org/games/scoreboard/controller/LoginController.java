package org.games.scoreboard.controller;

import org.games.scoreboard.model.CredentialsRequestBody;
import org.games.scoreboard.security.SessionManager;
import org.games.scoreboard.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SessionManager sessionManager;


    @RequestMapping(value = "/login", consumes = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<String> postLogin(
            @RequestBody CredentialsRequestBody credentialsRequestBody) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsRequestBody.getUsername(), credentialsRequestBody.getPassword()));


        String sessionKey = sessionManager.generateSessionKey((UserPrincipal) authentication.getPrincipal());

        return new ResponseEntity<>(sessionKey, HttpStatus.ACCEPTED);
    }

}
