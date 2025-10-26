package object.brick;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class StrongBrick extends Brick {

    private BufferedImage strongImage; // Ảnh 2 máu (Cam)
    private BufferedImage damagedImage; // Ảnh 1 máu (Vàng)

    // NƠI ĐẶT ĐƯỜNG DẪN ẢNH
    private static final String STRONG_PATH = "/assets/images/brick_orange.png";
    private static final String DAMAGED_PATH = "/assets/images/brick_yellow.png";

    public StrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 2, "Strong");

        // Tải ảnh
        try (InputStream is1 = getClass().getResourceAsStream(STRONG_PATH);
             InputStream is2 = getClass().getResourceAsStream(DAMAGED_PATH)) {

            if (is1 != null) strongImage = ImageIO.read(is1);
            if (is2 != null) damagedImage = ImageIO.read(is2);

            if (strongImage == null) System.err.println("LỖI: Không tìm thấy ảnh gạch cam.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            BufferedImage currentImage = null;

            if (hp == 2) {
                currentImage = strongImage;
                g.setColor(new Color(255, 128, 0)); // Màu dự phòng Cam
            } else { // hp == 1
                currentImage = damagedImage;
                g.setColor(Color.YELLOW); // Màu dự phòng Vàng
            }

            if (currentImage != null) {
                g.drawImage(currentImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }

            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}