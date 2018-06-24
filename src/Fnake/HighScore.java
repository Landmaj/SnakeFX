package Fnake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HighScore implements Serializable {
    private List<Score> scores;

    HighScore() {
        this.scores = new ArrayList<>();
    }

    List<Score> getScores() {
        return scores;
    }

    void addScore(Score score) {
        this.scores.add(score);
    }

    void sortScores() {
        this.scores.sort(Collections.reverseOrder());
    }
}
