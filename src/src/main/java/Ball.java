import java.awt.*;

public class Ball extends MovableObject {

    public Ball(int x, int y, int width, int height, int speed, int dx, int dy) {
        super(x, y, width, height, speed, dx, dy);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDx( int dx) {
        this.dx = dx;
    }

    public void setDy( int dy) {
        this.dy = dy;
    }

    @Override
    public void move() {
        x += dx * speed;
        y += dy * speed;
    }


    public void bounceOffObject(GameObject obj) {
        Rectangle ballRect = new Rectangle(x, y, width, height);
        Rectangle objRect = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());

        if (ballRect.intersects(objRect)) {
            Rectangle intersection = ballRect.intersection(objRect);
            if (intersection.width > intersection.height) {
                dy = -dy;
            } else {
                dx = -dx;
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
        g.fillOval(x, y, width, height);
    }
}
