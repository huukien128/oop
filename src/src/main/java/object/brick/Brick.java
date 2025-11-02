package object.brick;

import object.GameObject;

/**
 * Lớp trừu tượng Brick đại diện cho một viên gạch trong trò chơi.
 * Lớp này quản lý điểm máu (hitPoints) và trạng thái hủy diệt của gạch.
 */
public abstract class Brick extends GameObject {
    private int hitPoints;
    private String type;

    /**
     * Khởi tạo đối tượng Brick.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param hitPoints Số lần va chạm tối đa mà gạch có thể chịu được.
     * @param type Loại gạch (ví dụ: "Normal", "Strong").
     */
    public Brick(int x, int y, int width, int height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    /**
     * Gạch chịu một va chạm, giảm điểm máu đi 1.
     */
    public void takeHit() { hitPoints--; }

    /**
     * Lấy điểm máu hiện tại của gạch.
     * @return Điểm máu (hitPoints).
     */
    public int getHitPoints() { return hitPoints; }

    /**
     * Kiểm tra xem gạch đã bị phá hủy hay chưa (điểm máu <= 0).
     * @return True nếu gạch đã bị phá hủy.
     */
    public boolean isDestroyed() { return hitPoints <= 0; }

    /**
     * Lấy loại gạch.
     * @return Loại gạch dưới dạng chuỗi.
     */
    public String getType() { return type; }
}