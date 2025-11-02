package object.paddle;

import object.MovableObject;

import java.awt.*;

/**
 * Lớp Paddle đại diện cho thanh đỡ (vợt) mà người chơi điều khiển.
 * Nó quản lý vị trí, di chuyển ngang, và các trạng thái đặc biệt như khả năng bắn laser.
 */
public class Paddle extends MovableObject {
    private final int defaultWidth;
    private boolean isLaserReady = false;
    private long laserActivationTime = 0;
    private int dx = 0;

    private final Image paddleImage;

    /**
     * Khởi tạo Paddle.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     * @param width Chiều rộng ban đầu của paddle.
     * @param height Chiều cao của paddle.
     * @param speed Tốc độ di chuyển ngang.
     */
    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
        this.defaultWidth = width;
        this.paddleImage = loadImage("/images/paddle.png");
    }

    /**
     * Lấy chiều rộng mặc định của paddle (trước khi có power-up thay đổi).
     * @return Chiều rộng mặc định.
     */
    public int getDefaultWidth() { return defaultWidth; }

    /**
     * Thiết lập trạng thái sẵn sàng bắn laser của paddle.
     * @param active True nếu laser sẵn sàng, False nếu không.
     */
    public void setActiveLaser(boolean active) {
        this.isLaserReady = active;
        if (active) {
            this.laserActivationTime = System.currentTimeMillis();
        } else {
            this.laserActivationTime = 0;
        }
    }

    /**
     * Kiểm tra xem paddle có đang ở trạng thái sẵn sàng bắn laser không.
     * @return True nếu sẵn sàng bắn laser.
     */
    public boolean isLaserReady() { return isLaserReady; }

    /**
     * Lấy thời điểm laser được kích hoạt (dùng để kiểm soát độ trễ bắn).
     * @return Thời điểm kích hoạt laser (miligiây).
     */
    public long getLaserActivationTime() { return laserActivationTime; }

    /**
     * Thiết lập hướng di chuyển ngang cho paddle.
     * @param dx Giá trị vận tốc X (-1: trái, 1: phải, 0: dừng).
     */
    public void setDx(int dx) { this.dx = dx; }

    /**
     * Cập nhật vị trí X của paddle dựa trên vận tốc và tốc độ.
     * @see object.MovableObject#move()
     */
    @Override
    public void move() {
        setX(getX() + dx * getSpeed());
    }

    /**
     * Cập nhật logic của paddle, gọi phương thức move().
     * @see object.GameObject#update()
     */
    @Override
    public void update() { move(); }

    /**
     * Vẽ paddle lên màn hình. Ưu tiên vẽ bằng hình ảnh, nếu không thì vẽ bằng hình chữ nhật màu trắng.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.paddleImage != null) {
            g.drawImage(this.paddleImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}