import java.awt.*;
import java.util.List;

public class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 1;
    private List<Ball> balls;

    public FastBallPowerUp(int x, int y, int width, int height, List<Ball> balls) {
        super(x, y, width, height, "FastBall", 5000);
        this.balls = balls;
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
            // Chỉ giảm tốc độ nếu nó đã được tăng lên
            if (currentBall.getSpeed() > 2) { // Giả định tốc độ ban đầu là 2
                currentBall.setSpeed(currentBall.getSpeed() - BOOST_AMOUNT);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("F", x + 5, y + 15);
    }
}