package object.ball;

import object.GameObject;
import object.MovableObject;

import java.awt.*;

public class Ball extends MovableObject {

    private final Image ballImage;

    public Ball(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height, speed, dx, dy);

        this.ballImage = loadImage("/images/ball.png");
    }

    @Override
    public void move() {
        // CHỈ DI CHUYỂN NẾU CÓ HƯỚNG
        if (getDx() != 0 || getDy() != 0) {
            setX(getX() + getDx() * getSpeed());
            setY(getY() + getDy() * getSpeed());
        }
    }


    public void bounceOffObject(GameObject obj) {
        Rectangle ballRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle objRect = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());

        if (ballRect.intersects(objRect)) {
            Rectangle intersection = ballRect.intersection(objRect);
            if (intersection.width > intersection.height) {
                setDy(-getDy());
            } else {
                setDx(-getDx());
            }
        }
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics g) {
        if (this.ballImage != null) {
            g.drawImage(this.ballImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
        }
    }
}