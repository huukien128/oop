package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;

// Kế thừa từ lớp chung xử lý đồ họa
public class FastBallPowerUp extends GraphicalPowerUp {
    private static final int BOOST_AMOUNT = 1;
    private List<Ball> balls;

    // ĐƯỜNG DẪN ẢNH TƯƠNG ĐỐI
    private static final String IMAGE_PATH = "/assets/images/fastball.png";

    public FastBallPowerUp(int x, int y, int width, int height, List<Ball> balls) {
        // Gọi constructor lớp cha: (vị trí, kích thước, tên, thời gian, path)
        super(x, y, width, height, "FastBall", 5000, IMAGE_PATH);
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
            if (currentBall.getSpeed() > 2) {
                currentBall.setSpeed(currentBall.getSpeed() - BOOST_AMOUNT);
            }
        }
    }

    // Phương thức render đã được kế thừa và xử lý trong GraphicalPowerUp
}