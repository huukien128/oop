import java.awt.*;

class Paddle extends MovableObject {
    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    @Override
    public void move() {
        // Di chuyển được điều khiển bằng phím bấm, không cần logic trong hàm này
    }

    @Override
    public void update() {
        // Logic cập nhật trạng thái paddle (nếu cần)
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
}
