import javax.swing.*;
import java.awt.*;

public abstract class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected GameObject() {
    }

    public void update() {}
    public void draw(Graphics g) {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean checkCollision(GameObject obj) {
        return new Rectangle(x, y, width, height).intersects(new
                Rectangle(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight()));
    }
}
