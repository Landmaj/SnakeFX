package Fnake;

class GameOptions {
    static final GameSize SMALL = new GameSize(20, 20);
    static final GameSize MEDIUM = new GameSize(30, 30);
    static final GameSize LARGE = new GameSize(50, 50);

    static final GameSpeed FAST = new GameSpeed(75, "fast");
    static final GameSpeed NORMAL = new GameSpeed(125, "normal");
    static final GameSpeed SLOW = new GameSpeed(200, "slow");
}

class GameSize extends Tuple {
    GameSize(int x, int y) {
        super(x, y);
    }

    public String toString() {
        return this.getX() + "x" + this.getY();
    }
}

class GameSpeed {
    private int speed;
    private String name;

    GameSpeed(int speed, String name) {
        this.speed = speed;
        this.name = name;
    }

    int getSpeed() {
        return speed;
    }

    public String toString() {
        return this.name;
    }
}