package object;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;

/**
 * Lớp trừu tượng GameObject đại diện cho một đối tượng cơ bản trong trò chơi.
 * Lớp này quản lý tọa độ, kích thước, và cung cấp các chức năng cơ bản như tải ảnh và kiểm tra va chạm.
 */
public abstract class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    private BufferedImage image;

    /**
     * Constructor chính để khởi tạo GameObject với vị trí và kích thước.
     * @param x Tọa độ X của đối tượng.
     * @param y Tọa độ Y của đối tượng.
     * @param width Chiều rộng của đối tượng.
     * @param height Chiều cao của đối tượng.
     */
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor mặc định (dùng cho các đối tượng được khởi tạo sau đó).
     */
    public GameObject() {
    }

    /**
     * Tải một hình ảnh từ đường dẫn tài nguyên.
     * @param path Đường dẫn đến tệp hình ảnh (ví dụ: "/images/icon.png").
     * @return Đối tượng Image đã tải, hoặc null nếu có lỗi.
     */
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

    /**
     * Lấy tọa độ X của đối tượng.
     * @return Tọa độ X.
     */
    public int getX() { return x; }

    /**
     * Lấy tọa độ Y của đối tượng.
     * @return Tọa độ Y.
     */
    public int getY() { return y; }

    /**
     * Lấy chiều rộng của đối tượng.
     * @return Chiều rộng.
     */
    public int getWidth() { return width; }

    /**
     * Lấy chiều cao của đối tượng.
     * @return Chiều cao.
     */
    public int getHeight() { return height; }

    /**
     * Lấy hình ảnh (BufferedImage) của đối tượng.
     * @return Đối tượng BufferedImage.
     */
    public BufferedImage getImage() { return image; }

    /**
     * Thiết lập tọa độ X cho đối tượng.
     * @param x Tọa độ X mới.
     */
    public void setX(int x) { this.x = x; }

    /**
     * Thiết lập tọa độ Y cho đối tượng.
     * @param y Tọa độ Y mới.
     */
    public void setY(int y) { this.y = y; }

    /**
     * Thiết lập chiều rộng cho đối tượng.
     * @param width Chiều rộng mới.
     */
    public void setWidth(int width) { this.width = width; }

    /**
     * Thiết lập chiều cao cho đối tượng.
     * @param height Chiều cao mới.
     */
    public void setHeight(int height) { this.height = height; }

    /**
     * Cập nhật trạng thái của đối tượng (dành cho logic di chuyển, hoạt ảnh, v.v.).
     * Phương thức này cần được các lớp con ghi đè nếu đối tượng có logic cập nhật.
     */
    public void update() {}

    /**
     * Kiểm tra va chạm giữa đối tượng hiện tại và một GameObject khác.
     * @param obj Đối tượng khác cần kiểm tra va chạm.
     * @return True nếu có va chạm, ngược lại là False.
     */
    public boolean checkCollision(GameObject obj) {
        return new Rectangle(x, y, width, height).intersects(new
                Rectangle(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight()));
    }

    /**
     * Phương thức trừu tượng để vẽ (render) đối tượng lên màn hình.
     * Các lớp con phải cài đặt phương thức này.
     * @param g Đối tượng Graphics để vẽ.
     */
    public abstract void render(Graphics g);
}