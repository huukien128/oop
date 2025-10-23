package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final int EXPAND_AMOUNT = 50;

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() + EXPAND_AMOUNT);
        paddle.setX(paddle.getX() - EXPAND_AMOUNT / 2);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() - EXPAND_AMOUNT);
        paddle.setX(paddle.getX() + EXPAND_AMOUNT / 2);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString("E", getX() + 5, getY() + 15);
    }
}