package object;

/**
 * Lớp trừu tượng MovableObject đại diện cho các đối tượng trong game có khả năng di chuyển.
 * Lớp này mở rộng GameObject và thêm các thuộc tính về vận tốc (dx, dy) và tốc độ (speed).
 */
public abstract class MovableObject extends GameObject {
    private int dx;
    private int dy;
    private int speed;

    /**
     * Khởi tạo một MovableObject.
     * @param x Tọa độ X của đối tượng.
     * @param y Tọa độ Y của đối tượng.
     * @param width Chiều rộng của đối tượng.
     * @param height Chiều cao của đối tượng.
     * @param speed Tốc độ di chuyển cơ bản của đối tượng.
     * @param dx Vận tốc theo trục X (hướng ngang).
     * @param dy Vận tốc theo trục Y (hướng dọc).
     */
    public MovableObject(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height);
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Định nghĩa cách đối tượng di chuyển. Đây là phương thức trừu tượng cần được cài đặt bởi lớp con.
     */
    public void move() {
    }

    /**
     * Lấy vận tốc hiện tại theo trục X.
     * @return Vận tốc theo trục X.
     */
    public int getDx() { return dx; }

    /**
     * Lấy vận tốc hiện tại theo trục Y.
     * @return Vận tốc theo trục Y.
     */
    public int getDy() { return dy; }

    /**
     * Lấy tốc độ di chuyển cơ bản.
     * @return Tốc độ di chuyển.
     */
    public int getSpeed() { return speed; }

    /**
     * Thiết lập vận tốc theo trục X.
     * @param dx Vận tốc mới theo trục X.
     */
    public void setDx(int dx) { this.dx = dx; }

    /**
     * Thiết lập vận tốc theo trục Y.
     * @param dy Vận tốc mới theo trục Y.
     */
    public void setDy(int dy) { this.dy = dy; }

    /**
     * Thiết lập tốc độ di chuyển cơ bản.
     * @param speed Tốc độ mới.
     */
    public void setSpeed(int speed) { this.speed = speed; }
}