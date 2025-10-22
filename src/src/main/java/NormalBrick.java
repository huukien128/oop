import java.awt.*;

public class NormalBrick extends Brick {
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