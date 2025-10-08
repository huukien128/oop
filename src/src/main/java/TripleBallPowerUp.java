public class TripleBallPowerUp extends PowerUp {

    public TripleBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height, "TripleBall", 0);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {

        Game.balls.remove(ball);

        int startX = paddle.x + paddle.width / 2 - ball.width / 2;
        int startY = paddle.y - ball.height;
        int speed = ball.speed;

        Ball ball1 = new Ball(startX, startY, ball.width, ball.height);
        ball1.dx = 0;
        ball1.dy = -speed;

        Ball ball2 = new Ball(startX, startY, ball.width, ball.height);
        ball2.dx = (int) (-speed / Math.sqrt(2));
        ball2.dy = (int) (-speed / Math.sqrt(2));

        Ball ball3 = new Ball(startX, startY, ball.width, ball.height);
        ball3.dx = (int) (speed / Math.sqrt(2));
        ball3.dy = (int) (-speed / Math.sqrt(2));

        Game.balls.add(ball1);
        Game.balls.add(ball2);
        Game.balls.add(ball3);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("3x", x + 3, y + 15);
    }
}
