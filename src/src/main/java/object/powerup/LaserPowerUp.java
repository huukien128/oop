package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

/**
 * PowerUp LaserPowerUp kích hoạt khả năng bắn laser cho Paddle.
 * Hiệu ứng này là vĩnh viễn (hoặc cho đến khi mất mạng).
 */
public class LaserPowerUp extends PowerUp {

    private final Image powerUpImage;

    /**
     * Khởi tạo PowerUp LaserShot.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     */
    public LaserPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "LaserShot", 100);
        this.powerUpImage = loadImage("/images/powerup_laser.png");
    }

    /**
     * Áp dụng hiệu ứng: Kích hoạt khả năng bắn laser cho Paddle.
     * @param paddle Đối tượng Paddle bị ảnh hưởng.
     * @param ball Đối tượng Ball (không bị ảnh hưởng trực tiếp).
     * @see object.powerup.PowerUp#applyEffect(Paddle, Ball)
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setActiveLaser(true);
    }

    /**
     * LaserPowerUp không có hiệu ứng loại bỏ theo thời gian.
     * @param paddle Đối tượng Paddle.
     * @param ball Đối tượng Ball.
     * @see object.powerup.PowerUp#removeEffect(Paddle, Ball)
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {}

    /**
     * Vẽ PowerUp LaserPowerUp lên màn hình.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(255, 165, 0));
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("L", getX() + 5, getY() + 15);
        }
    }
}