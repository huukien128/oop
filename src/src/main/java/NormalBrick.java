import java.awt.*;

class NormalBrick extends Brick {
    // Sửa hàm khởi tạo để nhận tham số 'hp'
    public NormalBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp, "Normal");
    }

    @Override
    public void update() { }

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            g.setColor(Color.YELLOW);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}