import java.awt.*;

public class LifeUpPowerUp extends PowerUp {

    public LifeUpPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "LifeUp", 0);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawString("L", x + 5, y + 15);
    }
}