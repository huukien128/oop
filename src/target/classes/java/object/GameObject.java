package object;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;

public abstract class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    private BufferedImage image;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObject() {
    }

    public Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                Image loadedImage = ImageIO.read(is);
                if (loadedImage != null) {
                    System.out.println("Tải ảnh " + path + " thành công!");
                    return loadedImage;
                } else {
                    System.err.println("LỖI Tải ảnh: Lỗi định dạng tệp hoặc tệp rỗng " + path);
                    return null;
                }
            } else {
                System.err.println("LỖI Tải ảnh: Không tìm thấy tệp " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("LỖI Tải ảnh: Lỗi IO khi đọc tệp " + path + ": " + e.getMessage());
            return null;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public BufferedImage getImage() { return image; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    public void update() {}

    public boolean checkCollision(GameObject obj) {
        return new Rectangle(x, y, width, height).intersects(new
                Rectangle(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight()));
    }

    public abstract void render(Graphics g);
}