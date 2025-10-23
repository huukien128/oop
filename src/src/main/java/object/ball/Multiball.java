package object.ball;

import object.paddle.Paddle;
import object.powerup.PowerUp;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Multiball extends PowerUp {
    private List<Ball> balls;
    private int numberOfNewBalls;
    private static final int DEFAULT_DURATION = 10000;

    public Multiball(int x, int y, int width, int height, List<Ball> balls, int newBallsCount) {
        super(x, y, width, height, "object.ball.Multiball", DEFAULT_DURATION);
        this.balls = balls;
        this.numberOfNewBalls = newBallsCount;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        Random random = new Random();
        int baseSpeed = ball.getSpeed();

        for (int i = 0; i < numberOfNewBalls; i++) {
            Ball newBall = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), baseSpeed, 0, 0);

            int dx = random.nextBoolean() ? 1 : -1;
            int dy = -1;

            newBall.setDx(dx);
            newBall.setDy(dy);

            balls.add(newBall);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(numberOfNewBalls), getX() + getWidth() / 4, getY() + getHeight() / 2 + 5);
    }
}