package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;

public class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 1;
    private List<Ball> balls;

    private final Image powerUpImage;

    public FastBallPowerUp(int x, int y, int width, int height, List<Ball> balls) {
        super(x, y, width, height, "FastBall", 5000);
        this.balls = balls;
        this.powerUpImage = loadImage("/images/powerup_fast.png");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            currentBall.setSpeed(currentBall.getSpeed() + BOOST_AMOUNT);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            if (currentBall.getSpeed() > 2) {
                currentBall.setSpeed(currentBall.getSpeed() - BOOST_AMOUNT);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("F", getX() + 5, getY() + 15);
        }
    }
}