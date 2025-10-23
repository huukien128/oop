package game;

import manager.LevelManager;
import object.ball.Multiball;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class GameManager extends JPanel implements KeyListener, Runnable {
    protected static final int GAME_WIDTH = 800;
    protected static final int GAME_HEIGHT = 600;
    private static final int DELAY = 10;

    private final int LASER_SHOT_DELAY = 1000;
    private final int LASER_WIDTH = 20;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private int lives;
    private String gameState;

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
            backgroundImage = ImageIO.read(new File("../../imagines/background.png"));
        } catch (IOException e) {
            System.err.println("Lỗi tải ảnh background: " + e.getMessage());
            e.printStackTrace();
        }

        initGame();
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }

    private void initGame() {
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 140, 50, PADDLE_SPEED);
        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2 - 10), paddle.getY() - 20, 15, 15, BALL_START_SPEED, 1, -1));
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = "playing";

        levelManager = new LevelManager(GAME_WIDTH, GAME_HEIGHT);
        bricks = levelManager.createBricksForCurrentLevel();
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
                balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2 - 10), paddle.getY() - 20, 15, 15, BALL_START_SPEED, 1, -1));
                paddle.setActiveLaser(false);
                laserBeam = null;
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
                    Random rand = new Random();
                    if (rand.nextInt(100) < POWERUP_DROP_CHANCE) {
                        int powerUpType = rand.nextInt(4);
                        if (powerUpType == 0) {
                            powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, balls));
                        } else if (powerUpType == 1) {
                            powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else if (powerUpType == 2) {
                            powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
                        } else {
                            powerUps.add(new LaserPowerUp(brick.getX(), brick.getY(), 20, 20));
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
                balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2 - 10), paddle.getY() - 20, 20, 20, BALL_START_SPEED, 1, -1));
                paddle.setActiveLaser(false);
                laserBeam = null;
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
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }

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

        if (laserBeam != null) {
            laserBeam.render(g);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.setColor(Color.RED);
        g.drawString("Lives: " + lives, 10, 40);
        g.setColor(Color.BLUE);
        g.drawString("Level: " + levelManager.getCurrentLevel(), 10, 60);

        String puStatus = activePowerUp != null ? activePowerUp.getType() :
                (paddle.isLaserReady() ? "Laser Pending" : "None");
        g.drawString("object.powerup.PowerUp Active: " + puStatus, 10, 80);

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
                Thread.sleep(DELAY);
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