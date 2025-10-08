public class ExpandPaddlePowerUp extends PowerUp {
    private static final int EXPAND_AMOUNT = 50;

    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "ExpandPaddle", 5000);
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
