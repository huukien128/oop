package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final int EXPAND_AMOUNT = 50;

    private final Image powerUpImage;

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000);
        this.powerUpImage = loadImage("/images/powerup_expand.png");
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
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("E", getX() + 5, getY() + 15);
        }
    }
}