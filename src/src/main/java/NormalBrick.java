import java.awt.*;

class NormalBrick extends Brick {

    public NormalBrick(int x, int y, int width, int height) {
        super(x, y, width, height, 1, "Normal");
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
