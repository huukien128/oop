public class FastBallPowerUp extends PowerUp {
    private static final int BOOST_AMOUNT = 3;

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "FastBall", 5000);
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
