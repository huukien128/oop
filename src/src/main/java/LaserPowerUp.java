import java.awt.*;

class LaserPowerUp extends PowerUp {

    public LaserPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "LaserShot", 100);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setActiveLaser(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {}

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 165, 0)); // Màu cam
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString("L", x + 5, y + 15);
    }
}

