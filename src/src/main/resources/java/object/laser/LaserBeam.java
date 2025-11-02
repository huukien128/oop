package object.laser;

import object.GameObject;

import java.awt.*;

public class LaserBeam extends GameObject {
    private final int DURATION = 500;
    private long startTime;

    private final Image laserImage;

    public LaserBeam(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.laserImage = loadImage("/images/laser.png");
        this.startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (this.laserImage != null) {
            g.drawImage(this.laserImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.RED.brighter());
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}