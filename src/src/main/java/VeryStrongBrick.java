import java.awt.*;

class VeryStrongBrick extends Brick {
    public VeryStrongBrick(int x, int y, int width, int height) {
        super(x, y, width, height, 3, "VeryStrong");
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            if (hp == 3) g.setColor(Color.RED);
            else if (hp == 2) g.setColor(new Color(255, 128, 0));
            else g.setColor(Color.YELLOW);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}