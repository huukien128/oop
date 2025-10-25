package object.brick;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class NormalBrick extends Brick {

    private BufferedImage brickImage;
    private static final String IMAGE_PATH = "/assets/images/brick_yellow.png"; // <-- NƠI ĐẶT ĐƯỜNG DẪN ẢNH

    public NormalBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp, "Normal");

        try (InputStream is = getClass().getResourceAsStream(IMAGE_PATH)) {
            if (is != null) {
                brickImage = ImageIO.read(is);
            } else {
                System.err.println("LỖI: Không tìm thấy ảnh gạch vàng.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() { }

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            if (brickImage != null) {
                g.drawImage(brickImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}