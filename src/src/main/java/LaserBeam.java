import java.awt.*;

class LaserBeam extends GameObject {
    private final int DURATION = 500;
    private long startTime;

    public LaserBeam(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED.brighter());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}