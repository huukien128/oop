import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager extends JPanel implements KeyListener, Runnable {
    protected static final int GAME_WIDTH = 800;
    protected static final int GAME_HEIGHT = 600;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private String gameState;

    private PowerUp activePowerUp;
    private long powerUpStartTime;

    public GameManager() {
        initGame();
        setFocusable(true);
        addKeyListener(this);
    }

    private void initGame() {
        int PADDLE_SPEED = 100;
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 150, 50, PADDLE_SPEED);
        balls = new ArrayList<>();
        int BALL_START_SPEED = 2;
        balls.add(new Ball(GAME_WIDTH / 2 - 7, GAME_HEIGHT / 2 - 7, 20, 20, BALL_START_SPEED, 1, -1));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        gameState = "playing";

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                if (i % 3 == 0) {
                    bricks.add(new NormalBrick(j * 80 + 35, i * 30 + 50, 70, 20));
                } else if (i % 3 == 1) {
                    bricks.add(new StrongBrick(j * 80 + 35, i * 30 + 50, 70, 20));
                } else {
                    bricks.add(new VeryStrongBrick(j * 80 + 35, i * 30 + 50, 70, 20));
                }
            }
        }
    }

    public void startGame() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateGame() {
        if (!gameState.equals("playing")) {
            return;
        }

        // Cập nhật vị trí của paddle
        paddle.update();

        // Lặp qua tất cả các quả bóng để cập nhật vị trí và va chạm
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();

            // Xử lý va chạm với tường (trái, phải và trên cùng)
            if (ball.getX() <= 0 || ball.getX() >= GAME_WIDTH - ball.getWidth()) {
                ball.dx = -ball.dx;
            }
            if (ball.getY() <= 0) {
                ball.dy = -ball.dy;
            }

            // Xử lý va chạm bóng với các đối tượng khác
            checkBallCollisions(ball);

            // Kiểm tra xem bóng có rơi khỏi màn hình không
            if (ball.getY() > GAME_HEIGHT) {
                ballIterator.remove();
            }
        }

        // Kiểm tra điều kiện Game Over: khi không còn quả bóng nào
        if (balls.isEmpty()) {
            gameState = "gameOver";
        }

        // Cập nhật vị trí và xử lý va chạm của các power-up đang rơi
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update();

            // Va chạm giữa paddle và power-up
            if (pu.checkCollision(paddle)) {
                if (pu instanceof Multiball) {
                    if (!balls.isEmpty()) {
                        pu.applyEffect(paddle, balls.get(0));
                    }
                } else {
                    if (activePowerUp != null) {
                        activePowerUp.removeEffect(paddle, balls.get(0));
                    }
                    activePowerUp = pu;
                    if (!balls.isEmpty()) {
                        activePowerUp.applyEffect(paddle, balls.get(0));
                    }
                    powerUpStartTime = System.currentTimeMillis();
                }
                powerUpIterator.remove();
            }
        }

        checkPowerUpDuration();
        checkGameOver();
    }

    private void checkBallCollisions(Ball ball) {
        // Va chạm giữa bóng và paddle
        if (ball.checkCollision(paddle)) {
            ball.bounceOffObject(paddle);
        }

        // Va chạm giữa bóng và các viên gạch
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (!brick.isDestroyed() && ball.checkCollision(brick)) {
                ball.bounceOffObject(brick);
                brick.takeHit();
                if (brick.isDestroyed()) {
                    score += 10;
                    Random rand = new Random();
                    int POWERUP_DROP_CHANCE = 30;
                    if (rand.nextInt(100) < POWERUP_DROP_CHANCE) {
                        int powerUpType = rand.nextInt(3);
                        if (powerUpType == 0) {
                            powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else if (powerUpType == 1) {
                            powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else {
                            powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
                        }
                    }
                    brickIterator.remove();
                }
            }
        }
    }

    private void checkPowerUpDuration() {
        if (activePowerUp != null) {
            if (System.currentTimeMillis() - powerUpStartTime > activePowerUp.duration) {
                if (!balls.isEmpty()) {
                    activePowerUp.removeEffect(paddle, balls.get(0));
                }
                activePowerUp = null;
            }
        }
    }

    public void checkGameOver() {
        if (bricks.isEmpty()) {
            gameState = "gameWin";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g) {
        paddle.render(g);
        for (Ball ball : balls) {
            ball.render(g);
        }
        for (Brick brick : bricks) {
            brick.render(g);
        }
        for (PowerUp pu : powerUps) {
            pu.render(g);
        }
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);

        if (gameState.equals("gameOver")) {
            g.drawString("Game Over!", GAME_WIDTH / 2 - 40, GAME_HEIGHT / 2);
        } else if (gameState.equals("gameWin")) {
            g.drawString("You Win!", GAME_WIDTH / 2 - 40, GAME_HEIGHT / 2);
        }
    }

    @Override
    public void run() {
        while (!gameState.equals("gameOver") && !gameState.equals("gameWin")) {
            updateGame();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if (paddle.getX() > 0) {
                paddle.moveLeft();
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            if (paddle.getX() < GAME_WIDTH - paddle.getWidth()) {
                paddle.moveRight();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}