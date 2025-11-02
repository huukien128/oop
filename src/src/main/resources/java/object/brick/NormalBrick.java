package object.brick;

import java.awt.*;

public class NormalBrick extends Brick {

    private final Image brickImage;

    public NormalBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp, "Normal");
        this.brickImage = loadImage("/images/brick_1.png");
    }

    @Override
    public void update() { }

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            if (this.brickImage != null) {
                g.drawImage(this.brickImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}