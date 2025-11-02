package object.powerup;

import object.GameObject;
import object.paddle.Paddle;
import object.ball.Ball;

import java.awt.*;

/**
 * Lớp trừu tượng PowerUp đại diện cho một vật phẩm tăng sức mạnh (power-up) rơi xuống trong trò chơi.
 * Lớp này quản lý loại, thời gian hiệu lực và hành vi di chuyển cơ bản của PowerUp.
 */
public abstract class PowerUp extends GameObject {
    private String type;
    private int duration;

    /**
     * Khởi tạo PowerUp.
     *
     * @param x Tọa độ X.
     * @param y Tọa độ Y.
     * @param width Chiều rộng.
     * @param height Chiều cao.
     * @param type Loại PowerUp (ví dụ: "Multiball", "ExpandPaddle").
     * @param duration Thời gian hiệu lực (miligiây).
     */
    public PowerUp(int x, int y, int width, int height, String type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    /**
     * Phương thức trừu tượng để áp dụng hiệu ứng của PowerUp lên game (thường là paddle hoặc ball).
     * @param paddle Đối tượng Paddle bị ảnh hưởng.
     * @param ball Đối tượng Ball bị ảnh hưởng (hoặc ball chính).
     */
    public abstract void applyEffect(Paddle paddle, Ball ball);

    /**
     * Phương thức trừu tượng để loại bỏ hiệu ứng của PowerUp khỏi game khi hết thời gian.
     * @param paddle Đối tượng Paddle bị ảnh hưởng.
     * @param ball Đối tượng Ball bị ảnh hưởng (hoặc ball chính).
     */
    public abstract void removeEffect(Paddle paddle, Ball ball);

    /**
     * Cập nhật trạng thái của PowerUp. PowerUp tự động rơi xuống.
     * @see object.GameObject#update()
     */
    @Override
    public void update() {
        setY(getY() + 1);
    }

    /**
     * Phương thức vẽ (render) PowerUp lên màn hình.
     * Các lớp con cần cài đặt để hiển thị hình ảnh cụ thể.
     * @param g Đối tượng Graphics để vẽ.
     * @see object.GameObject#render(Graphics)
     */
    @Override
    public void render(Graphics g) {

    }

    /**
     * Lấy loại PowerUp.
     * @return Chuỗi biểu thị loại PowerUp.
     */
    public String getType() {
        return type;
    }

    /**
     * Lấy thời gian hiệu lực của PowerUp (miligiây).
     * @return Thời gian hiệu lực.
     */
    public int getDuration() {
        return duration;
    }
}