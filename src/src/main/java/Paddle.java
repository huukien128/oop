import java.awt.*;

class Paddle extends MovableObject {
    private final int defaultWidth;
    private boolean isLaserReady = false;
    private long laserActivationTime = 0;

    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
        this.defaultWidth = width;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setActiveLaser(boolean active) {
        this.isLaserReady = active;
        if (active) {
            this.laserActivationTime = System.currentTimeMillis();
        } else {
            this.laserActivationTime = 0;
        }
    }

    public boolean isLaserReady() { return isLaserReady; }
    public long getLaserActivationTime() { return laserActivationTime; }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    @Override
    public void move() {
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
}
