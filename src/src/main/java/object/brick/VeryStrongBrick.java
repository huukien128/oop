package object.brick;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class VeryStrongBrick extends Brick {

    private BufferedImage veryStrongImage; // Ảnh 3 máu (Đỏ)
    private BufferedImage strongImage; // Ảnh 2 máu (Cam)
    private BufferedImage normalImage; // Ảnh 1 máu (Vàng)

    // NƠI ĐẶT ĐƯỜNG DẪN ẢNH
    private static final String RED_PATH = "/assets/images/brick_red.png";
    private static final String ORANGE_PATH = "/assets/images/brick_orange.png";
    private static final String YELLOW_PATH = "/assets/images/brick_yellow.png";

    public VeryStrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 3, "VeryStrong");

        // Tải ảnh
        try (InputStream is1 = getClass().getResourceAsStream(RED_PATH);
             InputStream is2 = getClass().getResourceAsStream(ORANGE_PATH);
             InputStream is3 = getClass().getResourceAsStream(YELLOW_PATH)) {

            if (is1 != null) veryStrongImage = ImageIO.read(is1);
            if (is2 != null) strongImage = ImageIO.read(is2);
            if (is3 != null) normalImage = ImageIO.read(is3);

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

            if (hp == 3) {
                currentImage = veryStrongImage;
                g.setColor(Color.RED); // Dự phòng Đỏ
            } else if (hp == 2) {
                currentImage = strongImage;
                g.setColor(new Color(255, 128, 0)); // Dự phòng Cam
            } else { // hp == 1
                currentImage = normalImage;
                g.setColor(Color.YELLOW); // Dự phòng Vàng
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