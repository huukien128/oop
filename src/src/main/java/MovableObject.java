public abstract class MovableObject extends GameObject {
    protected int dx;
    protected int dy;
    protected int speed;

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
}