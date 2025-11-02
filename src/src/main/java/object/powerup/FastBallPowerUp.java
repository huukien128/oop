package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;

/**
 * PowerUp FastBallPowerUp làm tăng tốc độ di chuyển của tất cả các quả bóng hiện có trong game
 * trong một khoảng thời gian giới hạn.
 */
public class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 1;
    private List<Ball> balls;

    private final Image powerUpImage;

    /**
     * Khởi tạo PowerUp FastBall.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param balls Danh sách các quả bóng hiện có trong game.
     */
    public FastBallPowerUp(int x, int y, int width, int height, List<Ball> balls) {
        super(x, y, width, height, "FastBall", 5000);
        this.balls = balls;
        this.powerUpImage = loadImage("/images/powerup_fast.png");
    }

    /**
     * Áp dụng hiệu ứng: Tăng tốc độ của tất cả các quả bóng lên một lượng BOOST_AMOUNT.
     * @param paddle Đối tượng Paddle (không bị ảnh hưởng).
     * @param ball Đối tượng Ball (không bị ảnh hưởng trực tiếp).
     * @see object.powerup.PowerUp#applyEffect(Paddle, Ball)
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            currentBall.setSpeed(currentBall.getSpeed() + BOOST_AMOUNT);
        }
    }

    /**
     * Loại bỏ hiệu ứng: Giảm tốc độ của tất cả các quả bóng xuống một lượng BOOST_AMOUNT,
     * đảm bảo tốc độ không giảm xuống dưới mức cơ bản (2).
     * @param paddle Đối tượng Paddle (không bị ảnh hưởng).
     * @param ball Đối tượng Ball (không bị ảnh hưởng trực tiếp).
     * @see object.powerup.PowerUp#removeEffect(Paddle, Ball)
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            if (currentBall.getSpeed() > 2) {
                currentBall.setSpeed(currentBall.getSpeed() - BOOST_AMOUNT);
            }
        }
    }

    /**
     * Vẽ PowerUp FastBall lên màn hình.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
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