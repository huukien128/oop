package object.paddle;

import object.MovableObject;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Paddle extends MovableObject {
    private final int defaultWidth;
    private boolean isLaserReady = false;
    private long laserActivationTime = 0;
    private int dx = 0;

    private BufferedImage paddleImage;

    public Paddle(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, 0, 0);
        this.defaultWidth = width;


    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setActiveLaser(boolean active) {
        this.isLaserReady = active;
        if (active) {
            this.laserActivationTime = System.currentTimeMillis();
        } else {
            this.laserActivationTime = 0;
        }
    }

    public boolean isLaserReady() { return isLaserReady; }
    public long getLaserActivationTime() { return laserActivationTime; }

    public void setDx(int dx) {
        this.dx = dx;
    }

    @Override
    public void move() {
        setX(getX() + dx * getSpeed());
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}