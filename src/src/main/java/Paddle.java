import java.awt.*;

class Paddle extends MovableObject {
    private int dx = 0; // Vận tốc di chuyển theo chiều ngang

    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    @Override
    public void move() {
        x += dx * speed;
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
}