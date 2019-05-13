package org.games.scoreboard.controller;

import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.mapper.UserMapper;
import org.games.scoreboard.model.ScoreDTO;
import org.games.scoreboard.model.ScoreFilterType;
import org.games.scoreboard.security.UserPrincipal;
import org.games.scoreboard.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @RequestMapping(value = "/level/{levelId}/score/{value}", method = RequestMethod.PUT)
    public ResponseEntity<String> saveScoring(@PathVariable("levelId") int levelId, @PathVariable("value") int value,
                                              Authentication authentication) {
        UserDO user = getUserDO(authentication);
        scoreService.saveScore(user, levelId, value);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private UserDO getUserDO(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return UserMapper.convertToEntity(userPrincipal);
    }

    @RequestMapping(value = "/level/{levelId}/score", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<List<ScoreDTO>> getScoringForCertainLevel(
            @PathVariable("levelId") int levelId, @RequestParam(value = "filter") String filter) {
        ScoreFilterType scoreFilter = ScoreFilterType.from(filter);
        List<ScoreDTO> scores = scoreService.getHighestScoringForLevel(levelId, scoreFilter);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }
}
