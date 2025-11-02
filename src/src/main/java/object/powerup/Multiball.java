package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * PowerUp Multiball, khi được kích hoạt, tạo ra thêm các quả bóng mới trong trò chơi.
 * Hiệu ứng này là vĩnh viễn (hoặc cho đến khi các quả bóng mới bị mất).
 */
public class Multiball extends PowerUp {
    private List<Ball> balls;
    private int numberOfNewBalls;
    private static final int DEFAULT_DURATION = 10000;

    private final Image powerUpImage;

    /**
     * Khởi tạo PowerUp Multiball.
     *
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param balls Danh sách các quả bóng hiện có trong game.
     * @param newBallsCount Số lượng quả bóng mới được tạo ra khi kích hoạt.
     */
    public Multiball(int x, int y, int width, int height, List<Ball> balls, int newBallsCount) {
        super(x, y, width, height, "Multiball", DEFAULT_DURATION);
        this.balls = balls;
        this.numberOfNewBalls = newBallsCount;
        this.powerUpImage = loadImage("/images/powerup_multi.png");
    }

    /**
     * Áp dụng hiệu ứng: Tạo ra các quả bóng mới dựa trên số lượng đã định,
     * khởi tạo chúng ở vị trí bóng hiện tại và thêm vào danh sách game.
     * Hiệu ứng này không có thời gian kết thúc.
     *
     * @param paddle Đối tượng Paddle (không bị ảnh hưởng).
     * @param ball Đối tượng Ball chính (được dùng để lấy vị trí và tốc độ cơ bản).
     * @see object.powerup.PowerUp#applyEffect(Paddle, Ball)
     */
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

    /**
     * PowerUp Multiball không có hiệu ứng loại bỏ sau một thời gian,
     * vì nó tạo ra các đối tượng mới (bóng) thay vì thay đổi thuộc tính.
     *
     * @param paddle Đối tượng Paddle.
     * @param ball Đối tượng Ball.
     * @see object.powerup.PowerUp#removeEffect(Paddle, Ball)
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Không có hiệu ứng loại bỏ
    }

    /**
     * Vẽ PowerUp Multiball lên màn hình. Sử dụng hình ảnh nếu có, nếu không thì dùng hình tròn màu xanh lục bảo.
     *
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.CYAN);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(numberOfNewBalls), getX() + getWidth() / 4, getY() + getHeight() / 2 + 5);
        }
    }
}