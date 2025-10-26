package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

public class LaserPowerUp extends PowerUp {

    private final Image powerUpImage;

    public LaserPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "LaserShot", 100);
        this.powerUpImage = loadImage("/images/powerup_laser.png");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setActiveLaser(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {}

    @Override
    public void render(Graphics g) {
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(255, 165, 0));
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("L", getX() + 5, getY() + 15);
        }
    }
}