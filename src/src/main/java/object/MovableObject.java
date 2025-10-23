package object;

public abstract class MovableObject extends GameObject {
    private int dx;
    private int dy;
    private int speed;

    public MovableObject(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height);
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}