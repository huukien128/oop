import java.awt.*;

public abstract class PowerUp extends GameObject {
    private String type;
    private int duration;

    public PowerUp(int x, int y, int width, int height, String type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);

    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void update() {
        setY(getY() + 1);
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