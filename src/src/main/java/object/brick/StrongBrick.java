package object.brick;

import java.awt.*;

/**
 * Lớp StrongBrick đại diện cho loại gạch mạnh mẽ hơn, cần hai lần chạm để phá hủy (HP = 2).
 * Gạch này thay đổi hình ảnh (hoặc màu sắc dự phòng) sau lần chạm đầu tiên.
 */
public class StrongBrick extends Brick {

    private final Image[] brickImages;

    /**
     * Khởi tạo gạch mạnh.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param hp Điểm máu (thường là 2).
     */
    public StrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 2, "Strong");

        this.brickImages = new Image[2];

        this.brickImages[1] = loadImage("/images/brick_2.png");
        this.brickImages[0] = loadImage("/images/brick_1.png");
    }

    /**
     * Gạch mạnh không có logic cập nhật trạng thái trong vòng lặp game.
     * @see object.GameObject#update()
     */
    @Override
    public void update() {}

    /**
     * Vẽ gạch lên màn hình nếu nó chưa bị phá hủy. Hình ảnh (hoặc màu sắc) được chọn
     * dựa trên điểm máu hiện tại của gạch (HP=2: mạnh; HP=1: yếu).
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            Image currentImage = null;
            Color fallbackColor;

            if (hp == 2) {
                currentImage = this.brickImages[1];
                fallbackColor = Color.ORANGE;
            } else {
                currentImage = this.brickImages[0];
                fallbackColor = Color.YELLOW;
            }

            if (currentImage != null) {
                g.drawImage(currentImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(fallbackColor);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}