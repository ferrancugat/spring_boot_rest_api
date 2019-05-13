package org.games.scoreboard.repository;

import org.games.scoreboard.domain.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserDO, Integer> {

    Optional<UserDO> findOneByName(String name);
}

