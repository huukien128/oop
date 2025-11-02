package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

/**
 * Lớp trừu tượng GraphicalPowerUp mở rộng PowerUp, chịu trách nhiệm quản lý và hiển thị
 * hình ảnh (texture) cho các vật phẩm power-up.
 */
public abstract class GraphicalPowerUp extends PowerUp {

    private BufferedImage powerUpImage;
    private String imagePath;

    /**
     * Khởi tạo GraphicalPowerUp.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param type Loại PowerUp.
     * @param duration Thời gian hiệu lực (miligiây).
     * @param imagePath Đường dẫn đến hình ảnh PowerUp.
     */
    public GraphicalPowerUp(int x, int y, int width, int height, String type, int duration, String imagePath) {
        super(x, y, width, height, type, duration);
        this.imagePath = imagePath;
        loadImage();
    }

    /**
     * Tải hình ảnh (BufferedImage) từ đường dẫn tài nguyên đã cung cấp (imagePath).
     */
    private void loadImage() {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                powerUpImage = ImageIO.read(is);
            } else {
                System.err.println("LỖI: Không tìm thấy tài nguyên tại đường dẫn: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("Lỗi xử lý ảnh PowerUp: " + imagePath + " - " + e.getMessage());
        }
    }

    /**
     * Vẽ PowerUp lên màn hình. Ưu tiên vẽ bằng hình ảnh, nếu không tải được thì vẽ bằng hình tròn magenta.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (powerUpImage != null) {
            g.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString(getType().substring(0, 1), getX() + 5, getY() + 15);
        }
    }
}