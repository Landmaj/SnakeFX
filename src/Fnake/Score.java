package Fnake;

class Score {
    private String name;
    private int score;
    private int seconds;
    private boolean wallCollision;
    private double finalScore;

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
        this.score += 10;
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
}
