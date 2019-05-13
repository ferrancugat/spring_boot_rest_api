package org.games.scoreboard.mapper;

import org.games.scoreboard.domain.UserDO;
import org.games.scoreboard.security.UserPrincipal;

public class UserMapper {

    // an static method because there is no much logic ..fast coding
    public static UserDO convertToEntity(UserPrincipal userPrincipal) {
        UserDO userDO = new UserDO(userPrincipal.getUsername(), userPrincipal.getPassword());
        userDO.setId(userPrincipal.getId());
        return userDO;
    }
}
