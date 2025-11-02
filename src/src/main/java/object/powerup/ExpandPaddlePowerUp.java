package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

/**
 * PowerUp ExpandPaddlePowerUp làm tăng chiều rộng của Paddle trong một khoảng thời gian giới hạn.
 */
public class ExpandPaddlePowerUp extends PowerUp {
    private static final int EXPAND_AMOUNT = 50;

    private final Image powerUpImage;

    /**
     * Khởi tạo PowerUp ExpandPaddle.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     */
    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000);
        this.powerUpImage = loadImage("/images/powerup_expand.png");
    }

    /**
     * Áp dụng hiệu ứng: Tăng chiều rộng của Paddle và điều chỉnh vị trí X để giữ Paddle căn giữa.
     * @param paddle Đối tượng Paddle bị ảnh hưởng.
     * @param ball Đối tượng Ball (không bị ảnh hưởng).
     * @see object.powerup.PowerUp#applyEffect(Paddle, Ball)
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() + EXPAND_AMOUNT);
        paddle.setX(paddle.getX() - EXPAND_AMOUNT / 2);
    }

    /**
     * Loại bỏ hiệu ứng: Giảm chiều rộng của Paddle về kích thước ban đầu và điều chỉnh vị trí X.
     * @param paddle Đối tượng Paddle bị ảnh hưởng.
     * @param ball Đối tượng Ball (không bị ảnh hưởng).
     * @see object.powerup.PowerUp#removeEffect(Paddle, Ball)
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() - EXPAND_AMOUNT);
        paddle.setX(paddle.getX() + EXPAND_AMOUNT / 2);
    }

    /**
     * Vẽ PowerUp ExpandPaddle lên màn hình.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.powerUpImage != null) {
            g.drawImage(this.powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("E", getX() + 5, getY() + 15);
        }
    }
}