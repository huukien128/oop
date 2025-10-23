package object.ball;

import object.GameObject;
import object.MovableObject;

import java.awt.*;

public class Ball extends MovableObject {

    public Ball(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height, speed, dx, dy);
    }

    public int getSpeed() {
        return super.getSpeed();
    }

    public void setSpeed(int speed) {
        super.setSpeed(speed);
    }

    public void setDx( int dx) {
        super.setDx(dx);
    }

    public void setDy( int dy) {
        super.setDy(dy);
    }

    @Override
    public void move() {
        setX(getX() + getDx() * getSpeed());
        setY(getY() + getDy() * getSpeed());
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
        g.setColor(Color.RED);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}