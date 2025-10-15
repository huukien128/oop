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
    private int lives;
    private String gameState;

    private PowerUp activePowerUp;
    private long powerUpStartTime;

    public GameManager() {
        initGame();
        setFocusable(true);
        addKeyListener(this);
    }

    private void initGame() {
        int PADDLE_SPEED = 5;
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 150, 50, PADDLE_SPEED);
        balls = new ArrayList<>();
        int BALL_START_SPEED = 2;
        balls.add(new Ball(GAME_WIDTH / 2 - 7, GAME_HEIGHT / 2 - 7, 20, 20, BALL_START_SPEED, 1, -1));
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = "playing";

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                if (i % 3 == 0) {
                    bricks.add(new NormalBrick(j * 80 + 35, i * 30 + 50, 70, 20, 1));
                } else if (i % 3 == 1) {
                    bricks.add(new StrongBrick(j * 80 + 35, i * 30 + 50, 70, 20, 2));
                } else {
                    bricks.add(new VeryStrongBrick(j * 80 + 35, i * 30 + 50, 70, 20, 3));
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

        paddle.update();

        if (paddle.getX() < 0) {
            paddle.x = 0;
        } else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) {
            paddle.x = GAME_WIDTH - paddle.getWidth();
        }

        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();

            if (ball.getX() <= 0 || ball.getX() >= GAME_WIDTH - ball.getWidth()) {
                ball.dx = -ball.dx;
            }
            if (ball.getY() <= 0) {
                ball.dy = -ball.dy;
            }

            checkBallCollisions(ball);

            if (ball.getY() > GAME_HEIGHT) {
                ballIterator.remove();
            }
        }

        if (balls.isEmpty()) {
            lives--;
            if (lives > 0) {
                int BALL_START_SPEED = 2;
                balls.add(new Ball(GAME_WIDTH / 2, GAME_HEIGHT / 2 + 230, 20, 20, BALL_START_SPEED, 1, -1));
            } else {
                gameState = "gameOver";
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update();

            if (pu.checkCollision(paddle)) {
                if (!balls.isEmpty()) {
                    if (pu instanceof Multiball) {
                        pu.applyEffect(paddle, balls.get(0));
                    } else if (pu instanceof LifeUpPowerUp) { // Thêm logic xử lý LifeUpPowerUp
                        lives++;
                    } else {
                        if (activePowerUp != null) {
                            activePowerUp.removeEffect(paddle, balls.get(0));
                        }
                        activePowerUp = pu;
                        activePowerUp.applyEffect(paddle, balls.get(0));
                        powerUpStartTime = System.currentTimeMillis();
                    }
                }
                powerUpIterator.remove();
            }
        }

        checkPowerUpDuration();
        checkGameOver();
    }

    private void checkBallCollisions(Ball ball) {
        if (ball.checkCollision(paddle)) {
            ball.bounceOffObject(paddle);
        }

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
                        int powerUpType = rand.nextInt(4); // Tăng số lượng loại power-up
                        if (powerUpType == 0) {
                            powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, balls));
                        } else if (powerUpType == 1) {
                            powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else if (powerUpType == 2) {
                            powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
                        } else { // powerUpType == 3
                            powerUps.add(new LifeUpPowerUp(brick.getX(), brick.getY(), 20, 20));
                        }
                    }
                    brickIterator.remove();
                }
                break;
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
        g.setColor(Color.RED);
        g.drawString("Lives: " + lives, 10, 40);

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
            paddle.setDx(-1);
        }
        if (key == KeyEvent.VK_RIGHT) {
            paddle.setDx(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            paddle.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}