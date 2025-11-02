package object.ball;

import object.GameObject;
import object.MovableObject;

import java.awt.*;

/**
 * Lớp Ball đại diện cho quả bóng mà người chơi sử dụng để phá gạch.
 * Nó quản lý chuyển động, vận tốc và xử lý va chạm cơ bản.
 */
public class Ball extends MovableObject {

    private final Image ballImage;

    /**
     * Khởi tạo đối tượng Ball.
     * @param x Tọa độ X ban đầu.
     * @param y Tọa độ Y ban đầu.
     * @param width Chiều rộng của bóng.
     * @param height Chiều cao của bóng.
     * @param speed Tốc độ di chuyển cơ bản.
     * @param dx Vận tốc theo trục X (hướng ngang).
     * @param dy Vận tốc theo trục Y (hướng dọc).
     */
    public Ball(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height, speed, dx, dy);

        this.ballImage = loadImage("/images/ball.png");
    }

    /**
     * Cập nhật vị trí X và Y của bóng dựa trên vận tốc và tốc độ.
     * Bóng chỉ di chuyển nếu vận tốc (dx hoặc dy) khác 0.
     * @see object.MovableObject#move()
     */
    @Override
    public void move() {
        // CHỈ DI CHUYỂN NẾU CÓ HƯỚNG
        if (getDx() != 0 || getDy() != 0) {
            setX(getX() + getDx() * getSpeed());
            setY(getY() + getDy() * getSpeed());
        }
    }

    /**
     * Xử lý nảy của bóng khi va chạm với một GameObject (thường là Paddle).
     * Dựa trên kích thước giao nhau, xác định va chạm là ngang hay dọc để đảo chiều vận tốc.
     * @param obj Đối tượng mà bóng va chạm.
     */
    public void bounceOffObject(GameObject obj) {
        Rectangle ballRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle objRect = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());

        if (ballRect.intersects(objRect)) {
            Rectangle intersection = ballRect.intersection(objRect);
            if (intersection.width > intersection.height) {
                // Va chạm theo chiều ngang -> Đảo chiều dọc
                setDy(-getDy());
            } else {
                // Va chạm theo chiều dọc -> Đảo chiều ngang
                setDx(-getDx());
            }
        }
    }

    /**
     * Cập nhật logic của bóng, gọi phương thức move().
     * @see object.GameObject#update()
     */
    @Override
    public void update() {
        move();
    }

    /**
     * Vẽ bóng lên màn hình. Ưu tiên vẽ bằng hình ảnh, nếu không thì vẽ bằng hình tròn màu đỏ.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (this.ballImage != null) {
            g.drawImage(this.ballImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
        }
    }
}