import java.awt.*;

public abstract class PowerUp extends GameObject {
    private String type;
    public int duration;

    public PowerUp(int x, int y, int width, int height, String type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public void applyEffect(Paddle paddle, Ball ball) {}

    public void removeEffect(Paddle paddle, Ball ball) {}

    @Override
    public void update() {
        y += 1;
    }

    @Override
    public void render(Graphics g) {

    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }
}