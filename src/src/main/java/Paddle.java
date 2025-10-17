import java.awt.*;

class Paddle extends MovableObject {
    private final int defaultWidth;
    private boolean isLaserReady = false;
    private long laserActivationTime = 0;
    private int dx = 0;

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

    // Phương thức mới để đặt vận tốc
    public void setDx(int dx) {
        this.dx = dx;
    }

    // Xóa moveLeft() và moveRight() cũ vì chúng ta dùng update()

    @Override
    public void move() {
        // Áp dụng vận tốc cho vị trí
        x += dx * speed;
    }

    @Override
    public void update() {
        // Cập nhật vị trí trong mỗi khung hình dựa trên vận tốc đã đặt
        move();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
}