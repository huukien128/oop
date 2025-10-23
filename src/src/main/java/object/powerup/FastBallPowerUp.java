package object.powerup;

import object.ball.Ball;
import object.paddle.Paddle;

import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 1;
    private List<Ball> balls;
    private BufferedImage powerUpImage;

    public FastBallPowerUp(int x, int y, int width, int height, List<Ball> balls) {
        super(x, y, width, height, "FastBall", 5000);
        this.balls = balls;

        // TẢI ẢNH
        try {
            // THAY ĐƯỜNG DẪN ẢNH TẠI ĐÂY (VÍ DỤ: fastball.png)
            powerUpImage = ImageIO.read(new File("PASTE YOUR FASTBALL IMAGE PATH HERE"));
        } catch (IOException e) {
            System.err.println("Lỗi tải ảnh FastBall: " + e.getMessage());
        }
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            currentBall.setSpeed(currentBall.getSpeed() + BOOST_AMOUNT);
        }
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        for (Ball currentBall : balls) {
            if (currentBall.getSpeed() > 2) {
                currentBall.setSpeed(currentBall.getSpeed() - BOOST_AMOUNT);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (powerUpImage != null) {
            g.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("F", getX() + 5, getY() + 15);
        }
    }
}