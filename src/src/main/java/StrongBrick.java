import java.awt.*;

class StrongBrick extends Brick {
    public StrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 2, "Strong");
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            if (hp == 2) g.setColor(new Color(255, 128, 0));
            else g.setColor(Color.YELLOW);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
