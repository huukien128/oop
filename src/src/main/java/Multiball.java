import java.awt.*;
import java.util.List;

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
        // Tạo các quả bóng mới dựa trên tốc độ của quả bóng ban đầu
        for (int i = 0; i < numberOfNewBalls; i++) {
            Ball newBall = new Ball(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), ball.getSpeed(), 0, 0);

            // Gán hướng bay ngẫu nhiên cho các quả bóng mới
            double angle = (Math.random() * 60) + 15; // Góc từ 15 đến 75 độ
            double sign = (Math.random() > 0.5) ? 1 : -1;
            newBall.dx = (int) (sign * Math.cos(Math.toRadians(angle)) * newBall.getSpeed());
            newBall.dy = (int) (-Math.sin(Math.toRadians(angle)) * newBall.getSpeed());

            balls.add(newBall);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Multiball không cần gỡ bỏ hiệu ứng sau khi hết thời gian
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(numberOfNewBalls), x + width / 4, y + height / 2 + 5);
    }
}