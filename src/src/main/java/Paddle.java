import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

class Paddle extends MovableObject {
    private final int defaultWidth;
    private boolean isLaserReady = false;
    private long laserActivationTime = 0;
    private int dx = 0;

    private BufferedImage paddleImage;

    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
        this.defaultWidth = width;

        try (InputStream is = getClass().getResourceAsStream("/images/paddle.png")) {
            if (is != null) {
                paddleImage = ImageIO.read(is);
                System.out.println("Tải ảnh paddle thành công");
            } else {
                System.err.println("LỖI: Không tìm thấy tệp /images/paddle.png.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi xử lý ảnh paddle: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setActiveLaser(boolean active) {
        this.isLaserReady = active;
        if (active) {
            this.laserActivationTime = System.currentTimeMillis();
        } else {
            this.laserActivationTime = 0;
        }
    }

    public boolean isLaserReady() { return isLaserReady; }
    public long getLaserActivationTime() { return laserActivationTime; }

    // Phương thức mới để đặt vận tốc
    public void setDx(int dx) {
        this.dx = dx;
    }

    // Xóa moveLeft() và moveRight() cũ vì chúng ta dùng update()

    @Override
    public void move() {
        // Áp dụng vận tốc cho vị trí
        x += dx * speed;
    }

    @Override
    public void update() {
        // Cập nhật vị trí trong mỗi khung hình dựa trên vận tốc đã đặt
        move();
    }

    @Override
    public void render(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);
        }
    }
}