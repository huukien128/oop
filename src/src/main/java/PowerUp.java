import java.awt.*;

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
        y += 2;
    }

    @Override
    public abstract void render(Graphics g);

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }
}