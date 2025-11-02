package object.laser;

import object.GameObject;

import java.awt.*;

/**
 * Lớp LaserBeam đại diện cho tia laser được bắn ra từ Paddle, dùng để phá gạch.
 * Tia laser có thời gian tồn tại ngắn và không di chuyển.
 */
public class LaserBeam extends GameObject {
    private final int DURATION = 500;
    private long startTime;

    private final Image laserImage;

    /**
     * Khởi tạo LaserBeam.
     * @param x Tọa độ X (thường là giữa Paddle).
     * @param y Tọa độ Y (luôn là 0, bắn từ trên xuống dưới).
     * @param width Chiều rộng của tia laser.
     * @param height Chiều cao của tia laser (thường là đến vị trí Paddle).
     */
    public LaserBeam(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.laserImage = loadImage("/images/laser.png");
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Kiểm tra xem tia laser đã hết thời gian tồn tại (DURATION) hay chưa.
     * @return True nếu tia laser đã hết hạn.
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    /**
     * Tia laser không có logic cập nhật vị trí.
     * @see object.GameObject#update()
     */
    @Override
    public void update() {}

    /**
     * Vẽ tia laser lên màn hình. Ưu tiên vẽ bằng hình ảnh, nếu không thì vẽ bằng hình chữ nhật màu đỏ sáng.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.laserImage != null) {
            g.drawImage(this.laserImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.RED.brighter());
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}