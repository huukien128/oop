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

public abstract class GraphicalPowerUp extends PowerUp {

    protected BufferedImage powerUpImage;
    protected String imagePath;

    public GraphicalPowerUp(int x, int y, int width, int height, String type, int duration, String imagePath) {
        super(x, y, width, height, type, duration);
        this.imagePath = imagePath;
        loadImage();
    }

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

    @Override
    public void render(Graphics g) {
        if (powerUpImage != null) {
            // Vẽ ảnh
            g.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString(getType().substring(0, 1), getX() + 5, getY() + 15);
        }
    }

}