import java.awt.*;
import java.util.List;
import java.util.Random;

public class Multiball extends PowerUp {
    private List<Ball> balls;
    private int numberOfNewBalls;
    private static final int DEFAULT_DURATION = 10000;

    public Multiball(int x, int y, int width, int height, List<Ball> balls, int newBallsCount) {
        super(x, y, width, height, "Multiball", DEFAULT_DURATION);
        this.balls = balls;
        this.numberOfNewBalls = newBallsCount;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        Random random = new Random();
        int baseSpeed = ball.getSpeed();

        for (int i = 0; i < numberOfNewBalls; i++) {
            // Tạo một quả bóng mới tại vị trí của quả bóng ban đầu
            Ball newBall = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), baseSpeed, 0, 0);

            // Gán hướng ngẫu nhiên và đảm bảo vận tốc khác 0
            // dx có thể là -1 hoặc 1.
            int dx = random.nextBoolean() ? 1 : -1;
            // dy luôn luôn là -1 để bóng bay lên trên.
            int dy = -1;

            // setDx và setDy trong lớp Ball cần nhận giá trị int.
            newBall.setDx(dx);
            newBall.setDy(dy);

            balls.add(newBall);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Multiball không cần gỡ bỏ hiệu ứng.
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(numberOfNewBalls), x + width / 4, y + height / 2 + 5);
    }
}