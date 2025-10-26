package game;

import manager.LevelManager;
import object.powerup.Multiball;
import object.laser.LaserBeam;
import object.powerup.ExpandPaddlePowerUp;
import object.paddle.Paddle;
import object.ball.Ball;
import object.brick.Brick;
import object.powerup.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class GameManager extends JPanel implements KeyListener, Runnable {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    private static final int DELAY = 10;

    private final int LASER_SHOT_DELAY = 1000;
    private final int LASER_WIDTH = 20;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private int lives;
    private String gameState; // "ready", "playing", "paused", "gameOver", "gameWin"

    private LaserBeam laserBeam = null;

    private PowerUp activePowerUp;
    private long powerUpStartTime;

    private LevelManager levelManager;
    private BufferedImage backgroundImage;

    private final int PADDLE_SPEED = 10;
    private final int BALL_START_SPEED = 2;
    private final int POWERUP_DROP_CHANCE = 30;

    public GameManager() {
        try {
            InputStream is = getClass().getResourceAsStream("/images/background.png");

            if (is != null) {
                backgroundImage = ImageIO.read(is);
                System.out.println("Tải ảnh background thành công");
                is.close();
            } else {
                System.err.println("LỖI: Không tìm thấy tệp /images/background.png.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi xử lý ảnh background: " + e.getMessage());
            e.printStackTrace();
        }

        initGame();
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }

    private void createStartingBall() {
        Ball newBall = new Ball(
                paddle.getX() + (paddle.getWidth() / 2 - 10),
                paddle.getY() - 20,
                15, 15,
                BALL_START_SPEED,
                0, 0
        );
        balls.add(newBall);
    }

    private void initGame() {
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 140, 50, PADDLE_SPEED);
        balls = new ArrayList<>();

        createStartingBall();

        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = "ready";

        levelManager = new LevelManager(GAME_WIDTH, GAME_HEIGHT);
        bricks = levelManager.createBricksForCurrentLevel();
    }

    public void startGame() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateGame() {
        if (gameState.equals("paused")) {
            return;
        }

        if (gameState.equals("ready")) {
            paddle.update();
            if (paddle.getX() < 0) {
                paddle.setX(0);
            }
            else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) {
                paddle.setX(GAME_WIDTH - paddle.getWidth());
            }

            if (!balls.isEmpty()) {
                Ball ball = balls.get(0);
                ball.setX(paddle.getX() + (paddle.getWidth() / 2 - ball.getWidth() / 2));
                ball.setY(paddle.getY() - ball.getHeight());
            }
            return;
        }

        paddle.update();
        if (paddle.getX() < 0) {
            paddle.setX(0);
        }
        else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) {
            paddle.setX(GAME_WIDTH - paddle.getWidth());
        }

        handleLaserShot();

        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();

            if (ball.getX() <= 0 || ball.getX() >= GAME_WIDTH - ball.getWidth()) {
                ball.setDx(-ball.getDx());
            }
            if (ball.getY() <= 0) {
                ball.setDy(-ball.getDy());
            }

            checkBallCollisions(ball);

            if (ball.getY() > GAME_HEIGHT) {
                ballIterator.remove();
            }
        }

        if (balls.isEmpty()) {
            lives--;
            if (lives > 0) {
                createStartingBall();
                paddle.setActiveLaser(false);
                laserBeam = null;
                if (activePowerUp != null && !(activePowerUp instanceof Multiball) && !(activePowerUp instanceof LaserPowerUp)) {
                    activePowerUp.removeEffect(paddle, balls.get(0));
                    activePowerUp = null;
                }
                gameState = "ready";
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
                    } else if (pu instanceof LaserPowerUp) {
                        pu.applyEffect(paddle, balls.get(0));
                    } else {
                        if (activePowerUp != null) {
                            Ball currentBall = balls.isEmpty() ? null : balls.get(0);
                            if (currentBall != null) {
                                activePowerUp.removeEffect(paddle, currentBall);
                            }
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

    private void handleLaserShot() {
        if (paddle.isLaserReady() && laserBeam == null) {
            if (System.currentTimeMillis() - paddle.getLaserActivationTime() >= LASER_SHOT_DELAY) {
                int laserX = paddle.getX() + paddle.getWidth() / 2 - LASER_WIDTH / 2;
                int laserHeight = paddle.getY();
                laserBeam = new LaserBeam(laserX, 0, LASER_WIDTH, laserHeight);
                paddle.setActiveLaser(false);
            }
        }

        if (laserBeam != null) {
            if (laserBeam.isExpired()) {
                laserBeam = null;
            }
        }
    }

    private void checkBallCollisions(Ball ball) {

        if (ball.checkCollision(paddle)) {
            ball.bounceOffObject(paddle);
        }

        if (laserBeam != null) {
            Iterator<Brick> laserBrickIterator = bricks.iterator();
            while (laserBrickIterator.hasNext()) {
                Brick brick = laserBrickIterator.next();
                if (!brick.isDestroyed() && laserBeam.checkCollision(brick)) {
                    while (!brick.isDestroyed()) {
                        brick.takeHit();
                    }
                    score += 10;
                }
            }
            bricks.removeIf(Brick::isDestroyed);
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (!brick.isDestroyed() && ball.checkCollision(brick)) {
                ball.bounceOffObject(brick);
                brick.takeHit();
                if (brick.isDestroyed()) {
                    score += 10;

                    // THAY ĐỔI: Thêm kiểm tra viên gạch cuối cùng
                    // Nếu số lượng gạch còn lại (trừ gạch hiện tại sắp bị xóa) bằng 0, thì không rơi Power-Up
                    if (bricks.size() > 1) {
                        Random rand = new Random();
                        if (rand.nextInt(100) < POWERUP_DROP_CHANCE) {
                            int powerUpType = rand.nextInt(4);
                            if (powerUpType == 0) {
                                powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, balls));
                            } else if (powerUpType == 1) {
                                powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                            } else if (powerUpType == 2) {
                                powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
                            } else if (powerUpType == 3) {
                                powerUps.add(new LaserPowerUp(brick.getX(), brick.getY(), 20, 20));
                            }
                        }
                    } // Kết thúc kiểm tra Power-Up

                    brickIterator.remove();
                }
                break;
            }
        }
    }

    private void checkPowerUpDuration() {
        if (activePowerUp != null) {
            if (!(activePowerUp instanceof Multiball) && !(activePowerUp instanceof LaserPowerUp)) {
                if (System.currentTimeMillis() - powerUpStartTime > activePowerUp.getDuration()) {
                    if (!balls.isEmpty()) {
                        activePowerUp.removeEffect(paddle, balls.get(0));
                    }
                    activePowerUp = null;
                }
            }
        }
    }

    public void checkGameOver() {
        if (bricks.isEmpty()) {
            levelManager.nextLevel();
            bricks = levelManager.createBricksForCurrentLevel();
            if (bricks.isEmpty()) {
                gameState = "gameWin";
            } else {
                balls.clear();
                createStartingBall();
                paddle.setActiveLaser(false);
                laserBeam = null;
                if (activePowerUp != null && !(activePowerUp instanceof Multiball) && !(activePowerUp instanceof LaserPowerUp)) {
                    activePowerUp.removeEffect(paddle, balls.get(0));
                    activePowerUp = null;
                }
                gameState = "ready";
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }

        paddle.render(g2d);

        for (Ball ball : balls) {
            ball.render(g2d);
        }
        for (Brick brick : bricks) {
            brick.render(g2d);
        }
        for (PowerUp pu : powerUps) {
            pu.render(g2d);
        }

        if (laserBeam != null) {
            laserBeam.render(g2d);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 10, 20);
        g2d.setColor(Color.RED);
        g2d.drawString("Lives: " + lives, 10, 40);
        g2d.setColor(Color.BLUE);
        g2d.drawString("Level: " + levelManager.getCurrentLevel(), 10, 60);

        String puStatus = activePowerUp != null ? activePowerUp.getType() :
                (paddle.isLaserReady() ? "Laser Pending" : "None");
        g2d.drawString("PowerUp Active: " + puStatus, 10, 80);

        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        if (gameState.equals("ready")) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("PRESS SPACE TO START", GAME_WIDTH / 2 - 190, GAME_HEIGHT / 2);
        } else if (gameState.equals("paused")) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("PAUSED - PRESS SPACE TO RESUME", GAME_WIDTH / 2 - 270, GAME_HEIGHT / 2);
        } else if (gameState.equals("gameOver")) {
            g2d.drawString("Game Over!", GAME_WIDTH / 2 - 80, GAME_HEIGHT / 2);
        } else if (gameState.equals("gameWin")) {
            g2d.drawString("You Win!", GAME_WIDTH / 2 - 60, GAME_HEIGHT / 2);
        }
    }

    @Override
    public void run() {
        while (!gameState.equals("gameOver") && !gameState.equals("gameWin")) {
            updateGame();
            repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (gameState.equals("ready")) {
                if (!balls.isEmpty()) {
                    Ball ball = balls.get(0);
                    ball.setDx(1);
                    ball.setDy(-1);
                    gameState = "playing";
                }
            } else if (gameState.equals("playing")) {
                gameState = "paused";
            } else if (gameState.equals("paused")) {
                gameState = "playing";
            }
            return;
        }

        if (gameState.equals("ready") || gameState.equals("playing")) {
            if (key == KeyEvent.VK_LEFT) {
                paddle.setDx(-1);
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.setDx(1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameState.equals("ready") || gameState.equals("playing")) {
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                paddle.setDx(0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}