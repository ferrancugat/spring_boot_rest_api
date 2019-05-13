package org.games.scoreboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "scores")
public class ScoreDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scr_id")
    private Integer id;
    @ManyToOne(targetEntity = UserDO.class, optional = false)
    @JoinColumn(name = "usr_id")
    private UserDO user;
    @Column(name = "scr_value", nullable = false)
    private Integer value;
    @Column(name = "scr_level", nullable = false)
    private Integer level;
    @Column(name = "scr_createdat", nullable = false)
    private Date created;

    public ScoreDO(Integer level, Integer result, UserDO user) {
        this.level = level;
        this.value = result;
        this.user = user;
        this.created = new Date();
    }

    public ScoreDO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDO getUser() {
        return user;
    }

    public void setUser(UserDO user) {
        this.user = user;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
