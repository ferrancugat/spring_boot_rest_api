package org.games.scoreboard.model;

import org.springframework.util.StringUtils;

public enum ScoreFilterType {
    HIGHESTSCORE("highestscore"),
    NONE("none"),
    INVALID("invalid");

    final String val;

    ScoreFilterType(String val) {
        this.val = val;
    }

    public static ScoreFilterType from(String text) {
        if (StringUtils.isEmpty(text)) {
            return NONE;
        }
        for (ScoreFilterType scoreFilterType : values()) {
            if (scoreFilterType.val.equalsIgnoreCase(text)) return scoreFilterType;
        }
        return INVALID;
    }

    public String getVal() {
        return val;
    }
}
