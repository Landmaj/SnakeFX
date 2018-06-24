package Fnake;

import java.io.Serializable;

class Score implements Serializable, Comparable<Score> {
    private String name;
    private int score;
    private int seconds;
    private boolean wallCollision;
    private int finalScore;

    Score(String name, int score, int seconds, boolean wallCollision) {
        this.name = name;
        this.score = score;
        this.seconds = seconds;
        this.wallCollision = wallCollision;
    }

    void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    void addScore() {
        this.score += 1;
    }

    String getName() {
        return name;
    }

    int getScore() {
        return score;
    }

    int getSeconds() {
        return seconds;
    }

    boolean isWallCollision() {
        return wallCollision;
    }

    int getFinalScore() {
        return finalScore;
    }

    void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public int compareTo(Score score) {
        int compareScore = ((Score) score).getFinalScore();
        return this.finalScore - compareScore;
    }

}
