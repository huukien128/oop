package object.brick;

import java.awt.*;

/**
 * Lớp VeryStrongBrick đại diện cho loại gạch cực kỳ mạnh, cần ba lần chạm để phá hủy (HP = 3).
 * Gạch này thay đổi hình ảnh (hoặc màu sắc dự phòng) sau mỗi lần chạm, phản ánh độ hư hại.
 */
public class VeryStrongBrick extends Brick {

    private final Image[] brickImages;

    /**
     * Khởi tạo gạch cực mạnh.
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param hp Điểm máu (thường là 3).
     */
    public VeryStrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 3, "VeryStrong");

        this.brickImages = new Image[3];

        this.brickImages[2] = loadImage("/images/brick_3.png");
        this.brickImages[1] = loadImage("/images/brick_2.png");
        this.brickImages[0] = loadImage("/images/brick_1.png");
    }

    /**
     * Gạch cực mạnh không có logic cập nhật trạng thái trong vòng lặp game.
     * @see object.GameObject#update()
     */
    @Override
    public void update() {}

    /**
     * Vẽ gạch lên màn hình nếu nó chưa bị phá hủy. Hình ảnh (hoặc màu sắc) được chọn
     * dựa trên điểm máu hiện tại của gạch (HP=3: mới; HP=1: gần vỡ).
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            Image currentImage = null;
            Color fallbackColor;

            if (hp == 3) {
                currentImage = this.brickImages[2];
                fallbackColor = Color.RED;
            } else if (hp == 2) {
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