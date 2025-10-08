public class PowerUp extends GameObject {
    protected String type;
    protected int duration;

    public PowerUp(int x, int y, int width, int height, String type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);
    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void update() {
        y += 2; // Power-up rơi xuống
    }

    @Override
    public abstract void render(Graphics g);
}

/**
 * PowerUp tăng tốc độ của quả bóng.
 */
class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 3;

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "FastBall", 5000); // 5 giây
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.speed += BOOST_AMOUNT;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.speed -= BOOST_AMOUNT;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("F", x + 5, y + 15);
    }
}

/**
 * PowerUp tăng chiều rộng của thanh đỡ.
 */
class ExpandPaddlePowerUp extends PowerUp {
    private static final int EXPAND_AMOUNT = 50;

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000); // 5 giây
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.width += EXPAND_AMOUNT;
        paddle.x -= EXPAND_AMOUNT / 2;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.width -= EXPAND_AMOUNT;
        paddle.x += EXPAND_AMOUNT / 2;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString("E", x + 5, y + 15);
    }
}

/**
 * PowerUp nhân ba bóng – tạo 3 bóng bay theo 3 hướng khác nhau từ giữa thanh đỡ.
 */
class TripleBallPowerUp extends PowerUp {

    public TripleBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "TripleBall", 0); // hiệu ứng tức thời, không có thời gian kéo dài
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        // Xóa quả bóng hiện tại khỏi danh sách (để thay bằng 3 quả mới)
        Game.balls.remove(ball);

        // Lấy vị trí giữa của thanh đỡ
        int startX = paddle.x + paddle.width / 2 - ball.width / 2;
        int startY = paddle.y - ball.height; // phía trên thanh đỡ

        int speed = ball.speed; // giữ cùng tốc độ cũ

        // Quả bóng 1: đi thẳng lên
        Ball ball1 = new Ball(startX, startY, ball.width, ball.height);
        ball1.dx = 0;
        ball1.dy = -speed;

        // Quả bóng 2: đi chéo trái 45 độ
        Ball ball2 = new Ball(startX, startY, ball.width, ball.height);
        ball2.dx = (int) (-speed / Math.sqrt(2));
        ball2.dy = (int) (-speed / Math.sqrt(2));

        // Quả bóng 3: đi chéo phải 45 độ
        Ball ball3 = new Ball(startX, startY, ball.width, ball.height);
        ball3.dx = (int) (speed / Math.sqrt(2));
        ball3.dy = (int) (-speed / Math.sqrt(2));

        // Thêm 3 quả bóng mới vào danh sách trong Game
        Game.balls.add(ball1);
        Game.balls.add(ball2);
        Game.balls.add(ball3);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Không có hiệu ứng kéo dài
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("3x", x + 3, y + 15);
    }
}