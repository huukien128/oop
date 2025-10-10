import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager extends JPanel implements KeyListener, Runnable {
    protected static final int GAME_WIDTH = 600;
    protected static final int GAME_HEIGHT = 600;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private String gameState;

    // Biến để quản lý hiệu ứng power-up
    private PowerUp activePowerUp;
    private long powerUpStartTime;

    public GameManager() {
        initGame();
        setFocusable(true);
        addKeyListener(this);
    }

    private void initGame() {
        int PADDLE_SPEED = 10;
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 100, 20, PADDLE_SPEED);
        int BALL_START_SPEED = 5;
        ball = new Ball(GAME_WIDTH / 2 - 7, GAME_HEIGHT / 2 - 7, 15, 15, BALL_START_SPEED, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        gameState = "playing";

        // Tạo layout gạch
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (i % 2 == 0) {
                    bricks.add(new NormalBrick(j * 60 + 20, i * 30 + 50, 50, 20));
                } else {
                    bricks.add(new StrongBrick(j * 60 + 20, i * 30 + 50, 50, 20));
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

        // Cập nhật vị trí của bóng và paddle
        ball.update();

        // Cập nhật vị trí của các power-up đang rơi
        for (PowerUp pu : powerUps) {
            pu.update();
        }

        checkCollisions();
        checkPowerUpDuration();
        checkGameOver();
    }

    private void checkCollisions() {
        // Va chạm giữa bóng và paddle
        if (ball.checkCollision(paddle)) {
            ball.bounceOffObject(paddle);
        }

        // Va chạm giữa bóng và các viên gạch
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.checkCollision(brick)) {
                ball.bounceOffObject(brick);
                brick.takeHit();
                if (brick.isDestroyed()) {
                    score += 10;
                    // Tạo power-up ngẫu nhiên
                    Random rand = new Random();
                    // 30%
                    int POWERUP_DROP_CHANCE = 30;
                    if (rand.nextInt(100) < POWERUP_DROP_CHANCE) {
                        if (rand.nextBoolean()) {
                            powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else {
                            powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                        }
                    }
                }
            }
        }
        bricks.removeIf(Brick::isDestroyed);

        // Va chạm giữa paddle và power-up
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            if (pu.checkCollision(paddle)) {
                if (activePowerUp != null) {
                    activePowerUp.removeEffect(paddle, ball);
                }
                activePowerUp = pu;
                activePowerUp.applyEffect(paddle, ball);
                powerUpStartTime = System.currentTimeMillis();
                powerUpIterator.remove();
            }
        }
    }

    private void checkPowerUpDuration() {
        if (activePowerUp != null) {
            if (System.currentTimeMillis() - powerUpStartTime > activePowerUp.duration) {
                activePowerUp.removeEffect(paddle, ball);
                activePowerUp = null;
            }
        }
    }

    public void checkGameOver() {
        if (ball.getY() > GAME_HEIGHT) {
            gameState = "gameOver";
        }
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
        ball.render(g);
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