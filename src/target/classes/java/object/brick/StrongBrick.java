package object.brick;

import java.awt.*;

public class StrongBrick extends Brick {

    private final Image[] brickImages;

    public StrongBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, 2, "Strong");

        this.brickImages = new Image[2];

        this.brickImages[1] = loadImage("/images/brick_2.png");
        this.brickImages[0] = loadImage("/images/brick_1.png");
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            int hp = getHitPoints();
            Image currentImage = null;
            Color fallbackColor;

            if (hp == 2) {
                currentImage = this.brickImages[1];
                fallbackColor = Color.ORANGE;
            } else {
                currentImage = this.brickImages[0];
                fallbackColor = Color.YELLOW;
            }

            if (currentImage != null) {
                g.drawImage(currentImage, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(fallbackColor);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}