package object.brick;

import java.awt.*;

/**
 * Lớp NormalBrick đại diện cho loại gạch cơ bản, chỉ cần một lần chạm để phá hủy (HP = 1).
 */
public class NormalBrick extends Brick {

    private final Image brickImage;

    /**
     * Khởi tạo gạch thường.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param hp Điểm máu (thường là 1).
     */
    public NormalBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp, "Normal");
        this.brickImage = loadImage("/images/brick_1.png");
    }

    /**
     * Gạch thường không có logic cập nhật trạng thái trong vòng lặp game.
     * @see object.GameObject#update()
     */
    @Override
    public void update() { }

    /**
     * Vẽ gạch lên màn hình nếu nó chưa bị phá hủy. Ưu tiên vẽ bằng hình ảnh,
     * nếu không thì vẽ bằng hình chữ nhật màu vàng.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            if (this.brickImage != null) {
                g.drawImage(this.brickImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}