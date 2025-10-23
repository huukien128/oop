package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

public class LaserPowerUp extends PowerUp {

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
        g.setColor(new Color(255, 165, 0));
        g.fillOval(getX(), getY(), getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString("L", getX() + 5, getY() + 15);
    }
}