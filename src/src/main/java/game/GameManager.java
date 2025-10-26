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

    // Khởi tạo trạng thái game là "paused" (chờ bắt đầu)
    private String gameState = "paused";
    private boolean gameStarted = false;

    private LaserBeam laserBeam = null;

    private PowerUp activePowerUp;
    private long powerUpStartTime;

    private LevelManager levelManager;
    private BufferedImage backgroundImage;

    private final int PADDLE_SPEED = 10;
    private final int BALL_START_SPEED = 2;
    private final int POWERUP_DROP_CHANCE = 30;

    // Biến lưu trạng thái phím di chuyển
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Khởi tạo Random cho hướng bay bóng
    private final Random random = new Random();


    public GameManager() {
        try (InputStream is = getClass().getResourceAsStream("/images/background.png")) {
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                System.out.println("Tải ảnh background thành công");
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

    private void initGame() {
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 50, 140, 50, PADDLE_SPEED);
        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2 - 10), paddle.getY() - 20, 15, 15, BALL_START_SPEED, 0, 0));
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        gameState = "paused"; // Khởi tạo ban đầu là paused
        gameStarted = false;

        levelManager = new LevelManager(GAME_WIDTH, GAME_HEIGHT);
        bricks = levelManager.createBricksForCurrentLevel();
    }

    public void startGame() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Hàm helper để bắn bóng với hướng ngẫu nhiên.
     */
    private void launchBall(Ball ball) {
        /*int speed = ball.getSpeed();
        int dx = random.nextBoolean() ? speed : -speed;
        int dy = -speed;

        ball.setDx(dx);
        ball.setDy(dy);*/
        int dx = random.nextBoolean() ? 1 : -1;
        int dy = -1;

        ball.setDx(dx);
        ball.setDy(dy);
    }

    public void updateGame() {
        // Cập nhật vị trí bóng theo paddle khi game PAUSED
        if (gameState.equals("paused") && balls.size() == 1) {
            Ball ball = balls.get(0);
            ball.setX(paddle.getX() + (paddle.getWidth() / 2 - ball.getWidth() / 2));
            ball.setY(paddle.getY() - ball.getHeight());

            // Vẫn cho phép di chuyển paddle khi paused
            if (leftPressed) {
                paddle.setDx(-1);
            } else if (rightPressed) {
                paddle.setDx(1);
            } else {
                paddle.setDx(0);
            }
            paddle.update();
            // Giới hạn Paddle
            if (paddle.getX() < 0) {
                paddle.setX(0);
            }
            else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) {
                paddle.setX(GAME_WIDTH - paddle.getWidth());
            }
            return;
        }


        if (!gameState.equals("playing")) {
            return;
        }

        // LOGIC DI CHUYỂN PADDLE LIÊN TỤC KHI PHÍM ĐƯỢC GIỮ
        if (leftPressed) {
            paddle.setDx(-1);
        } else if (rightPressed) {
            paddle.setDx(1);
        } else {
            paddle.setDx(0);
        }

        paddle.update();

        // Giới hạn Paddle
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
                // MẤT MẠNG: TỰ ĐỘNG sinh bóng và bắn ngay lập tức
                // TỐC ĐỘ ĐỒNG NHẤT: BALL_START_SPEED = 2
                Ball newBall = new Ball(
                        paddle.getX() + (paddle.getWidth() / 2 - 10),
                        paddle.getY() - 20,
                        15, 15, BALL_START_SPEED, 0, 0);
                balls.add(newBall);

                paddle.setActiveLaser(false);
                laserBeam = null;

                // Bắn bóng tự động với hướng ngẫu nhiên
                launchBall(newBall);
                gameState = "playing"; // Tự động tiếp tục chơi

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
                        // Multiball: Tốc độ bóng mới sẽ lấy từ BALL_START_SPEED
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
            return;
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
                        int powerUpType = rand.nextInt(3); // Giảm số loại Power-Up còn 3
                        if (powerUpType == 0) {
                            powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, balls));
                        } else if (powerUpType == 1) {
                            powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                        } else {
                            // Multiball
                            powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
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
                balls.add(new Ball(
                        paddle.getX() + (paddle.getWidth() / 2 - 10),
                        paddle.getY() - 20,
                        15, 15, BALL_START_SPEED, 0, 0));
                paddle.setActiveLaser(false);
                laserBeam = null;
                gameState = "paused";
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

        if (gameState.equals("gameOver")) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER! Press SPACE to Play Again", GAME_WIDTH / 2 - 250, GAME_HEIGHT / 2);
        } else if (gameState.equals("gameWin")) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("YOU WIN! Press SPACE to Play Again", GAME_WIDTH / 2 - 250, GAME_HEIGHT / 2);
        } else if (gameState.equals("paused") && !gameStarted) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press SPACE to Start", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
        } else if (gameState.equals("paused") && gameStarted) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String msg = "PAUSED. Press SPACE to Continue";
            if (balls.size() == 1 && balls.get(0).getDy() == 0) {
                msg = "Level " + levelManager.getCurrentLevel() + " complete! Press SPACE to Launch Ball";
            }
            g.drawString(msg, GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + score, 10, 20);
        g.setColor(Color.RED);
        g.drawString("Lives: " + lives, 10, 40);
        g.setColor(Color.BLUE);
        g.drawString("Level: " + levelManager.getCurrentLevel(), 10, 60);

        String puStatus = activePowerUp != null ? activePowerUp.getType() :
                (paddle.isLaserReady() ? "Laser Pending" : "None");
        g.drawString("PowerUp Active: " + puStatus, 10, 80);

    }

    @Override
    public void run() {
        while (true) {
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

        // LOGIC CHỈ DÙNG SPACE ĐỂ CHƠI LẠI KHI THUA/THẮNG
        if (key == KeyEvent.VK_SPACE) {
            if (gameState.equals("gameOver") || gameState.equals("gameWin")) {
                initGame();
                gameStarted = false;
                return; // Chơi lại từ đầu
            }

            if (!gameStarted) {
                // Bắt đầu game lần đầu tiên
                gameState = "playing";
                gameStarted = true;
                if (!balls.isEmpty()) {
                    launchBall(balls.get(0)); // Bắn bóng ngẫu nhiên
                }
            } else if (gameState.equals("playing")) {
                // Tạm dừng game
                gameState = "paused";
            } else if (gameState.equals("paused")) {
                // Tiếp tục game (khi pause thủ công) hoặc Bắn bóng (sau khi qua màn)
                gameState = "playing";

                // Nếu bóng đang "dính" paddle (tức là dy=0), thì bắn nó đi
                if (balls.size() == 1 && balls.get(0).getDy() == 0) {
                    launchBall(balls.get(0));
                }
            }
        }

        // LOGIC ĐIỀU KHIỂN PADDLE (Sử dụng cờ leftPressed/rightPressed)
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Cập nhật cờ khi nhả phím
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}