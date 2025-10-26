package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;
import java.util.Random;

// Kế thừa từ lớp chung xử lý đồ họa
public class Multiball extends GraphicalPowerUp {
    private List<Ball> balls;
    private int numberOfNewBalls;
    private static final int DEFAULT_DURATION = 10000;

    // ĐƯỜNG DẪN ẢNH TƯƠNG ĐỐI
    private static final String IMAGE_PATH = "/assets/images/multiball.png";

    public Multiball(int x, int y, int width, int height, List<Ball> balls, int newBallsCount) {
        // Tên lớp được sửa lại thành "Multiball"
        super(x, y, width, height, "Multiball", DEFAULT_DURATION, IMAGE_PATH);
        this.balls = balls;
        this.numberOfNewBalls = newBallsCount;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        Random random = new Random();

        // Lấy tốc độ cơ bản (speed) hiện tại của quả bóng gốc
        int currentBallSpeed = ball.getSpeed();

        for (int i = 0; i < numberOfNewBalls; i++) {
            // Đảm bảo quả bóng mới có tốc độ (speed) giống quả bóng hiện tại
            Ball newBall = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), currentBallSpeed, 0, 0);

            // QUAN TRỌNG: dx và dy chỉ là hướng (1 hoặc -1)
            int dxDirection = random.nextBoolean() ? 1 : -1;
            int dyDirection = -1; // Hướng lên

            newBall.setDx(dxDirection);
            newBall.setDy(dyDirection);

            balls.add(newBall);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Không cần gỡ bỏ hiệu ứng
    }

    // Phương thức render đã được kế thừa từ GraphicalPowerUp
}