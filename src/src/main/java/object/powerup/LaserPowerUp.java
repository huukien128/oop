package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;

// Kế thừa từ lớp chung xử lý đồ họa
public class LaserPowerUp extends GraphicalPowerUp {

    // ĐƯỜNG DẪN ẢNH TƯƠNG ĐỐI
    private static final String IMAGE_PATH = "/assets/images/laser_pu.png";

    public LaserPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "LaserShot", 100, IMAGE_PATH);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.setActiveLaser(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {}

    // Phương thức render đã được kế thừa từ GraphicalPowerUp
}