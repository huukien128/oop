package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

// Kế thừa từ lớp chung xử lý đồ họa
public class ExpandPaddlePowerUp extends GraphicalPowerUp {
    private static final int EXPAND_AMOUNT = 30; // Giảm kích thước để tránh lỗi tràn màn hình

    // ĐƯỜNG DẪN ẢNH TƯƠNG ĐỐI
    private static final String IMAGE_PATH = "/assets/images/expand_pu.png";

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000, IMAGE_PATH);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(paddle.getWidth() + EXPAND_AMOUNT);
        paddle.setX(paddle.getX() - EXPAND_AMOUNT / 2);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Sử dụng chiều rộng mặc định (getDefaultWidth()) để reset chính xác
        paddle.setWidth(paddle.getDefaultWidth());
        paddle.setX(paddle.getX() + EXPAND_AMOUNT / 2);
    }

    // Phương thức render đã được kế thừa từ GraphicalPowerUp
}