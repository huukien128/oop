package game;

import manager.LevelManager;
import object.powerup.Multiball;
import object.laser.LaserBeam;
import object.powerup.ExpandPaddlePowerUp;
import object.paddle.Paddle;
import object.ball.Ball;
import object.brick.Brick;
import object.powerup.*;
import manager.MenuManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import Audio.SoundManager;

public class GameManager extends JPanel implements KeyListener, Runnable, MouseListener {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    private static final int DELAY = 10;

    private static final int COLLISION_WIDTH = GAME_WIDTH - 10;

    // QUẢN LÝ ĐƯỜNG DẪN HÌNH ẢNH
    private static final String BACKGROUND_IMAGE_PATH = "/images/background.png";
    private static final String MAIN_MENU_IMAGE_PATH = "/images/menu_main_bg.png"; // ẢNH MENU CHÍNH

    private final int LASER_SHOT_DELAY = 1000;
    private final int LASER_WIDTH = 20;

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private int score;
    private int lives;
    private String gameState;

    private String menuScreen;

    private LaserBeam laserBeam = null;

    private PowerUp activePowerUp;
    private long powerUpStartTime;

    private LevelManager levelManager;
    private BufferedImage backgroundImage;
    private BufferedImage mainMenuImage; // BIẾN LƯU ẢNH MENU CHÍNH
    private MenuManager menuManager;

    private int selectedMenuItem;
    private boolean isMuted;
    private String currentLanguage;

    // BIẾN HIỆU ỨNG FADE
    private int fadeAlpha = 255;
    private boolean isFadingOut = false;
    private boolean isFadingIn = false;
    private final int FADE_SPEED = 10; // Tốc độ fade (alpha steps)


    private final int PADDLE_SPEED = 10;
    private final int BALL_START_SPEED = 2;
    private final int POWERUP_DROP_CHANCE = 30;
    private SoundManager soundManager;

    // ĐƯỜNG DẪN ÂM THANH GỐC (KHÔNG THAY ĐỔI)
    private final String MUSIC_PATH = "/sound/music.wav";
    private final String LASER_PATH = "/sound/electric.wav";
    private final String HIT_PATH = "/sound/recover.wav";
    private final String LOSE_LIFE_PATH = "/sound/hurt.wav";
    private final String GAME_OVER_PATH = "/sound/lose.wav";
    private final String POWERUP_PICKUP_PATH = "/sound/powerup.wav";

    public GameManager() {
        try {
            // Tải background game chính
            InputStream is = getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH);
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                is.close();
            } else {
                System.err.println("LỖI: Không tìm thấy tệp " + BACKGROUND_IMAGE_PATH);
            }

            // Tải ảnh Menu Chính
            is = getClass().getResourceAsStream(MAIN_MENU_IMAGE_PATH);
            if (is != null) {
                mainMenuImage = ImageIO.read(is);
                System.out.println("Tải ảnh Menu Chính thành công");
                is.close();
            } else {
                System.err.println("CẢNH BÁO: Không tìm thấy tệp " + MAIN_MENU_IMAGE_PATH);
            }

        } catch (IOException e) {
            System.err.println("Lỗi xử lý ảnh background: " + e.getMessage());
            e.printStackTrace();
        }

        soundManager = new SoundManager();
        menuManager = new MenuManager();

        selectedMenuItem = MenuManager.MAIN_START;
        isMuted = false;
        menuScreen = MenuManager.SCREEN_MAIN;
        currentLanguage = "VI";

        soundManager.loadSound(MUSIC_PATH);
        soundManager.loadSound(LASER_PATH);
        soundManager.loadSound(HIT_PATH);
        soundManager.loadSound(LOSE_LIFE_PATH);
        soundManager.loadSound(GAME_OVER_PATH);
        soundManager.loadSound(POWERUP_PICKUP_PATH); // Tải âm thanh mới

        if (!isMuted) soundManager.playSound(MUSIC_PATH, true);

        initGame();
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
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
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 80, 140, 20, PADDLE_SPEED);
        balls = new ArrayList<>();

        createStartingBall();

        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;

        gameState = MenuManager.STATE_MENU;

        if (levelManager == null) {
            levelManager = new LevelManager(GAME_WIDTH, GAME_HEIGHT);
        }
        bricks = levelManager.createBricksForCurrentLevel();

        menuScreen = MenuManager.SCREEN_MAIN;
        selectedMenuItem = MenuManager.MAIN_START;
    }

    private void resetGameForPlay() {
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 80, 140, 20, PADDLE_SPEED);
        balls = new ArrayList<>();
        createStartingBall();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;

        bricks = levelManager.createBricksForCurrentLevel();

        // Bắt đầu màn chơi từ Menu chính -> Cần Fade In
        isFadingIn = true;
        fadeAlpha = 0;
        gameState = MenuManager.STATE_PLAYING; // Sẽ chuyển sang READY sau fade in
    }

    private void restartCurrentLevel() {
        // --- SỬA LỖI CHÍNH TẠI ĐÂY: Reset nhanh chóng ---

        // 1. RESET PADDLE VÀ BALLS
        paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 80, 140, 20, PADDLE_SPEED);
        balls.clear();
        createStartingBall();

        // 2. RESET POWERUPS VÀ EFFECT
        powerUps.clear();
        if (activePowerUp != null) {
            // Loại bỏ hiệu ứng cũ
            if (!balls.isEmpty()) activePowerUp.removeEffect(paddle, balls.get(0));
            activePowerUp = null;
        }
        paddle.setActiveLaser(false);
        laserBeam = null;


        // 3. TẢI LẠI GẠCH và CHUYỂN NGAY VỀ TRẠNG THÁI READY
        bricks = levelManager.createBricksForCurrentLevel();

        gameState = MenuManager.STATE_READY;

        // Đảm bảo không có Fade
        isFadingOut = false;
        isFadingIn = false;
        fadeAlpha = 255;

        if (isMuted) soundManager.stopSound(MUSIC_PATH);
        else soundManager.playSound(MUSIC_PATH, true);
    }

    // PHƯƠNG THỨC CHUNG ĐỂ BẮT ĐẦU FADE OUT
    private void startFadeOut(boolean isLifeLost) {
        if (isLifeLost) {
            if (!isMuted) soundManager.playSound(LOSE_LIFE_PATH, false);
        }
        isFadingOut = true;
        fadeAlpha = 255;
        balls.clear();
    }

    private void handleFadeEffect() {
        if (isFadingOut) {
            fadeAlpha -= FADE_SPEED;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                isFadingOut = false;

                // Reset vị trí paddle/ball VÀ TẢI LẠI MAP
                paddle = new Paddle(GAME_WIDTH / 2 - 50, GAME_HEIGHT - 80, 140, 20, PADDLE_SPEED);
                createStartingBall();

                // Tải lại map cho level hiện tại
                bricks = levelManager.createBricksForCurrentLevel();

                isFadingIn = true;
            }
        } else if (isFadingIn) {
            fadeAlpha += FADE_SPEED;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255;
                isFadingIn = false;
                gameState = MenuManager.STATE_READY; // Sẵn sàng chơi lại
            }
        }
    }


    public void startGame() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateGame() {
        if (gameState.equals(MenuManager.STATE_MENU) || gameState.equals(MenuManager.STATE_PAUSED) || gameState.equals(MenuManager.STATE_GAME_OVER) || gameState.equals(MenuManager.STATE_GAME_WIN)) {
            return;
        }

        // Xử lý Fade In/Out
        if (isFadingOut || isFadingIn) {
            handleFadeEffect();
            return; // Dừng logic game khi đang fade
        }


        if (gameState.equals(MenuManager.STATE_READY)) {
            paddle.update();
            if (paddle.getX() < 0) paddle.setX(0);
            else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) paddle.setX(GAME_WIDTH - paddle.getWidth());

            if (!balls.isEmpty()) {
                Ball ball = balls.get(0);
                ball.setX(paddle.getX() + (paddle.getWidth() / 2 - ball.getWidth() / 2));
                ball.setY(paddle.getY() - ball.getHeight());
            }
            return;
        }

        // Logic trạng thái "playing"
        paddle.update();
        if (paddle.getX() < 0) paddle.setX(0);
        else if (paddle.getX() + paddle.getWidth() > GAME_WIDTH) paddle.setX(GAME_WIDTH - paddle.getWidth());

        handleLaserShot();

        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();

            if (ball.getX() < 0) {
                ball.setX(0);
                ball.setDx(-ball.getDx());
            } else if (ball.getX() > COLLISION_WIDTH - ball.getWidth()) {
                ball.setX(COLLISION_WIDTH - ball.getWidth());
                ball.setDx(-ball.getDx());
            }

            if (ball.getY() < 0) {
                ball.setY(0);
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
                startFadeOut(true); // Bắt đầu Fade Out khi mất mạng
            } else {
                // GAME OVER
                soundManager.stopSound(MUSIC_PATH);
                if (!isMuted) soundManager.playSound(GAME_OVER_PATH, false);
                gameState = MenuManager.STATE_GAME_OVER;
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update();

            if (pu.checkCollision(paddle)) {
                if (!isMuted) soundManager.playSound(POWERUP_PICKUP_PATH, false); // Dùng âm thanh pickup

                if (!balls.isEmpty()) {
                    Ball mainBall = balls.get(0);

                    if (pu instanceof Multiball || pu instanceof LaserPowerUp) {
                        pu.applyEffect(paddle, mainBall);
                    } else {
                        if (activePowerUp != null) activePowerUp.removeEffect(paddle, mainBall);
                        activePowerUp = pu;
                        activePowerUp.applyEffect(paddle, mainBall);
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

                if (!isMuted) soundManager.playSound(LASER_PATH, false);
            }
        }

        if (laserBeam != null && laserBeam.isExpired()) {
            laserBeam = null;
        }
    }

    private void checkBallCollisions(Ball ball) {

        if (ball.checkCollision(paddle)) {
            if (ball.getDy() > 0) {

                ball.bounceOffObject(paddle);
                ball.setY(paddle.getY() - ball.getHeight());

                if (ball.getDy() >= 0) ball.setDy(-ball.getDy());
                if (Math.abs(ball.getDy()) > BALL_START_SPEED) ball.setDy(-BALL_START_SPEED);

                if (!isMuted) soundManager.playSound(HIT_PATH, false);
            }
        }

        if (laserBeam != null) {
            bricks.removeIf(brick -> {
                if (!brick.isDestroyed() && laserBeam.checkCollision(brick)) {
                    if (!isMuted) soundManager.playSound(HIT_PATH, false);

                    while (!brick.isDestroyed()) { brick.takeHit(); }
                    score += 10;
                    return true;
                }
                return false;
            });
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (!brick.isDestroyed() && ball.checkCollision(brick)) {

                if (!isMuted) soundManager.playSound(HIT_PATH, false);

                boolean hitVertical = ball.getDy() != 0;

                // LOGIC ĐẨY BÓNG VÀ ĐẢO CHIỀU ĐƠN GIẢN
                if (hitVertical) {
                    if (ball.getDy() > 0) {
                        // Va chạm mặt trên gạch: Đẩy bóng lên trên gạch
                        ball.setY(brick.getY() - ball.getHeight());
                    } else {
                        // Va chạm mặt dưới gạch: Đẩy bóng xuống dưới gạch
                        ball.setY(brick.getY() + brick.getHeight());
                    }
                    ball.setDy(-ball.getDy()); // Đảo chiều dọc
                } else {
                    if (ball.getDx() > 0) {
                        // Va chạm mặt trái gạch: Đẩy bóng sang trái gạch
                        ball.setX(brick.getX() - ball.getWidth());
                    } else {
                        // Va chạm mặt phải gạch: Đẩy bóng sang phải gạch
                        ball.setX(brick.getX() + brick.getWidth());
                    }
                    ball.setDx(-ball.getDx()); // Đảo chiều ngang
                }


                brick.takeHit();

                if (brick.isDestroyed()) {
                    score += 10;

                    if (bricks.size() > 1) {
                        Random rand = new Random();
                        if (rand.nextInt(100) < POWERUP_DROP_CHANCE) {
                            int type = rand.nextInt(4);
                            if (type == 0) powerUps.add(new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, balls));
                            else if (type == 1) powerUps.add(new ExpandPaddlePowerUp(brick.getX(), brick.getY(), 20, 20));
                            else if (type == 2) powerUps.add(new Multiball(brick.getX(), brick.getY(), 20, 20, balls, 2));
                            else if (type == 3) powerUps.add(new LaserPowerUp(brick.getX(), brick.getY(), 20, 20));
                        }
                    }
                    brickIterator.remove();
                }

                // ĐIỂM SỬA CHỮA QUAN TRỌNG: Ngắt vòng lặp ngay sau khi va chạm với viên gạch đầu tiên.
                break;
            }
        }
    }

    private void checkPowerUpDuration() {
        if (activePowerUp != null && !(activePowerUp instanceof Multiball) && !(activePowerUp instanceof LaserPowerUp)) {
            if (System.currentTimeMillis() - powerUpStartTime > activePowerUp.getDuration()) {
                if (!balls.isEmpty()) {
                    activePowerUp.removeEffect(paddle, balls.get(0));
                }
                activePowerUp = null;
            }
        }
    }

    public void checkGameOver() {
        if (bricks.isEmpty()) {
            levelManager.nextLevel();
            // THAY ĐỔI: Sử dụng Fade cho chuyển màn
            startFadeOut(false);

            // Xóa code reset cũ ở đây để tránh reset 2 lần:
            /*
            bricks = levelManager.createBricksForCurrentLevel();
            if (bricks.isEmpty()) { ... } else { ... }
            */
            return;
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

        // 1. VẼ BACKGROUND CHUNG
        if (gameState.equals(MenuManager.STATE_MENU) && menuScreen.equals(MenuManager.SCREEN_MAIN) && mainMenuImage != null) {
            g2d.drawImage(mainMenuImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }

        // 2. THIẾT LẬP ALPHA CHO PADDLE VÀ BALLS (Fade Effect)
        if (isFadingIn || isFadingOut) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) fadeAlpha / 255f));
        }


        // 3. VẼ CÁC OBJECT TRÒ CHƠI
        if (!gameState.equals(MenuManager.STATE_MENU) || (gameState.equals(MenuManager.STATE_MENU) && menuScreen.equals(MenuManager.SCREEN_OPTIONS))) {

            paddle.render(g2d);

            // Chỉ vẽ bóng khi không phải fade out
            if (!isFadingOut) {
                for (Ball ball : balls) {
                    ball.render(g2d);
                }
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

            // 4. ĐẶT LẠI ALPHA COMPOSITE (Nếu đã thay đổi)
            if (isFadingIn || isFadingOut) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }


            // Vẽ HUD
            if (!gameState.equals(MenuManager.STATE_MENU) && !gameState.equals(MenuManager.STATE_GAME_OVER) && !gameState.equals(MenuManager.STATE_GAME_WIN)) {
                g2d.setColor(Color.WHITE);
                g2d.drawString("Score: " + score, 10, 20);
                g2d.drawString("Lives: " + lives, 10, 40);
                g2d.drawString("Level: " + levelManager.getCurrentLevel(), 10, 60);

                String puStatus = activePowerUp != null ? activePowerUp.getType() :
                        (paddle.isLaserReady() ? "Laser Pending" : "None");
                g2d.drawString("PowerUp Active: " + puStatus, 10, 80);
            }
        }

        // 5. VẼ LỚP PHỦ CHO MENU
        if (gameState.equals(MenuManager.STATE_PAUSED) ||
                (gameState.equals(MenuManager.STATE_MENU) &&
                        (menuScreen.equals(MenuManager.SCREEN_OPTIONS) ||
                                menuScreen.equals(MenuManager.SCREEN_CREDITS) ||
                                menuScreen.equals(MenuManager.SCREEN_LEVEL_SELECT)))) {

            g2d.setColor(new Color(0, 0, 0, 180)); // Dùng màu đen bán trong suốt
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }


        // 6. VẼ NỘI DUNG MENU
        menuManager.drawMenuScreen(g2d, gameState, menuScreen, score, selectedMenuItem, levelManager.getCurrentLevel(), isMuted, currentLanguage);
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
                Thread.currentThread().interrupt();
            }
        }
    }

    private int getMenuCount() {
        if (menuScreen.equals(MenuManager.SCREEN_MAIN)) return MenuManager.MAIN_ITEMS_COUNT;
        if (menuScreen.equals(MenuManager.SCREEN_PAUSE)) return MenuManager.PAUSE_ITEMS_COUNT;
        if (menuScreen.equals(MenuManager.SCREEN_OPTIONS)) return MenuManager.OPTIONS_ITEMS_COUNT;
        if (menuScreen.equals(MenuManager.SCREEN_LEVEL_SELECT)) return MenuManager.LEVEL_ITEMS_COUNT;
        return 0;
    }

    private Rectangle getMenuItemBounds(String screen, int itemIndex) {
        if (screen.equals(MenuManager.SCREEN_CREDITS) || screen.equals(MenuManager.SCREEN_LEVEL_SELECT)) return new Rectangle(0, 0, 0, 0);

        int startY = GAME_HEIGHT / 2 - 80;
        int lineHeight = 35;
        int y = startY + itemIndex * lineHeight - 30;

        int width = 300;
        int height = 40;
        int x = GAME_WIDTH / 2 - width / 2;

        // Điều chỉnh cho Level Select 2 cột (Chỉ dùng cho logic click)
        if (screen.equals(MenuManager.SCREEN_LEVEL_SELECT)) {
            int half = MenuManager.MAX_LEVEL / 2;
            int col1X = GAME_WIDTH / 2 - 150;
            int col2X = GAME_WIDTH / 2 + 50;

            if (itemIndex < half) { // Cột 1
                x = col1X - 20;
            } else if (itemIndex < MenuManager.MAX_LEVEL) { // Cột 2
                x = col2X - 20;
            } else { // Back button
                x = GAME_WIDTH / 2 - 150;
            }
            y = itemIndex < MenuManager.MAX_LEVEL ? (GAME_HEIGHT / 2 - 80) + (itemIndex % half) * 35 - 30 :
                    (GAME_HEIGHT / 2 - 80) + half * 35 + 50 - 30;
            width = 160;
            height = 40;
            return new Rectangle(x, y, width, height);
        }

        return new Rectangle(x, y, width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Xử lý ESCAPE/ENTER để thoát khỏi Credits
        if (menuScreen.equals(MenuManager.SCREEN_CREDITS)) {
            if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER) {
                menuScreen = MenuManager.SCREEN_MAIN;
                selectedMenuItem = MenuManager.MAIN_CREDITS;
            }
            return;
        }

        // Xử lý ESCAPE trong Level Select
        if (menuScreen.equals(MenuManager.SCREEN_LEVEL_SELECT) && key == KeyEvent.VK_ESCAPE) {
            handleMenuSelection(MenuManager.SCREEN_LEVEL_SELECT, MenuManager.LEVEL_BACK);
            return;
        }


        // Xử lý PAUSE/ESCAPE
        if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
            if (gameState.equals(MenuManager.STATE_PLAYING) || gameState.equals(MenuManager.STATE_READY)) {
                gameState = MenuManager.STATE_PAUSED;
                menuScreen = MenuManager.SCREEN_PAUSE;
                selectedMenuItem = MenuManager.PAUSE_RESUME;
                return;
            } else if (gameState.equals(MenuManager.STATE_PAUSED)) {
                gameState = MenuManager.STATE_PLAYING;
                return;
            } else if (gameState.equals(MenuManager.STATE_MENU) && key == KeyEvent.VK_ESCAPE) {
                handleMenuSelection(MenuManager.SCREEN_MAIN, MenuManager.MAIN_EXIT);
                return;
            } else if (menuScreen.equals(MenuManager.SCREEN_OPTIONS) && gameState.equals(MenuManager.STATE_MENU)) {
                handleMenuSelection(MenuManager.SCREEN_OPTIONS, MenuManager.OPTIONS_BACK);
                return;
            }
        }

        // Xử lý điều hướng trong Menu (Chính, Pause, Options, Level Select)
        if (gameState.equals(MenuManager.STATE_MENU) || gameState.equals(MenuManager.STATE_PAUSED)) {
            int count = getMenuCount();

            if (key == KeyEvent.VK_UP) {
                selectedMenuItem = (selectedMenuItem - 1 + count) % count;
            } else if (key == KeyEvent.VK_DOWN) {
                selectedMenuItem = (selectedMenuItem + 1) % count;
            } else if (key == KeyEvent.VK_ENTER) {
                handleMenuSelection(menuScreen, selectedMenuItem);
            }
            return;
        }

        // Xử lý quay lại Menu sau Game Over/Win
        if (key == KeyEvent.VK_ENTER) {
            if (gameState.equals(MenuManager.STATE_GAME_OVER) || gameState.equals(MenuManager.STATE_GAME_WIN)) {
                initGame();
            }
            return;
        }

        // Xử lý Launch Ball và Pause bằng Space
        if (key == KeyEvent.VK_SPACE) {
            if (gameState.equals(MenuManager.STATE_READY)) {
                if (!balls.isEmpty()) {
                    Ball ball = balls.get(0);
                    ball.setDx(1);
                    ball.setDy(-1);
                    gameState = MenuManager.STATE_PLAYING;
                }
            } else if (gameState.equals(MenuManager.STATE_PLAYING)) {
                gameState = MenuManager.STATE_PAUSED; // Tạm dừng bằng Space
                menuScreen = MenuManager.SCREEN_PAUSE;
                selectedMenuItem = MenuManager.PAUSE_RESUME;
            }
            return;
        }

        // Xử lý điều khiển Paddle
        if (gameState.equals(MenuManager.STATE_READY) || gameState.equals(MenuManager.STATE_PLAYING)) {
            if (key == KeyEvent.VK_LEFT) {
                paddle.setDx(-1);
            }
            if (key == KeyEvent.VK_RIGHT) {
                paddle.setDx(1);
            }
        }
    }

    private void handleMenuSelection(String currentScreen, int selectedItem) {
        if (currentScreen.equals(MenuManager.SCREEN_MAIN)) {
            switch (selectedItem) {
                case MenuManager.MAIN_START:
                    resetGameForPlay();
                    break;
                case MenuManager.MAIN_LEVEL_SELECT:
                    menuScreen = MenuManager.SCREEN_LEVEL_SELECT;
                    selectedMenuItem = Math.max(0, levelManager.getCurrentLevel() - 1);
                    break;
                case MenuManager.MAIN_OPTIONS:
                    menuScreen = MenuManager.SCREEN_OPTIONS;
                    selectedMenuItem = MenuManager.OPTIONS_SOUND;
                    break;
                case MenuManager.MAIN_CREDITS:
                    menuScreen = MenuManager.SCREEN_CREDITS;
                    break;
                case MenuManager.MAIN_EXIT:
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }
                    break;
            }
        } else if (currentScreen.equals(MenuManager.SCREEN_LEVEL_SELECT)) { // LOGIC CHỌN LEVEL

            if (selectedItem >= 0 && selectedItem < MenuManager.MAX_LEVEL) {
                int levelToSet = selectedItem + 1;

                levelManager.setCurrentLevel(levelToSet);
                menuScreen = MenuManager.SCREEN_MAIN;
                selectedMenuItem = MenuManager.MAIN_LEVEL_SELECT;
            } else if (selectedItem == MenuManager.LEVEL_BACK) {
                menuScreen = MenuManager.SCREEN_MAIN;
                selectedMenuItem = MenuManager.MAIN_LEVEL_SELECT;
            }

        } else if (currentScreen.equals(MenuManager.SCREEN_PAUSE)) {
            switch (selectedItem) {
                case MenuManager.PAUSE_RESUME:
                    gameState = MenuManager.STATE_PLAYING;
                    break;
                case MenuManager.PAUSE_RESTART:
                    restartCurrentLevel();
                    break;
                case MenuManager.PAUSE_MAIN_MENU:
                    initGame();
                    break;
            }
        } else if (currentScreen.equals(MenuManager.SCREEN_OPTIONS)) {
            switch (selectedItem) {
                case MenuManager.OPTIONS_SOUND:
                    isMuted = !isMuted;
                    if (isMuted) {
                        soundManager.stopSound(MUSIC_PATH);
                    } else {
                        soundManager.playSound(MUSIC_PATH, true);
                    }
                    break;
                case MenuManager.OPTIONS_LANGUAGE:
                    currentLanguage = currentLanguage.equals("VI") ? "EN" : "VI";
                    break;
                case MenuManager.OPTIONS_BACK:
                    menuScreen = MenuManager.SCREEN_MAIN;
                    selectedMenuItem = MenuManager.MAIN_OPTIONS;
                    break;
            }
        }
    }

    // --- Xử lý MouseListener ---

    @Override
    public void mouseClicked(MouseEvent e) {
        if ((gameState.equals(MenuManager.STATE_MENU) || gameState.equals(MenuManager.STATE_PAUSED))
                && !menuScreen.equals(MenuManager.SCREEN_CREDITS)) {

            int mouseX = e.getX();
            int mouseY = e.getY();

            int count = getMenuCount();

            for (int i = 0; i < count; i++) {
                Rectangle bounds = getMenuItemBounds(menuScreen, i);

                if (bounds.contains(mouseX, mouseY)) {
                    selectedMenuItem = i;
                    handleMenuSelection(menuScreen, selectedMenuItem);
                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameState.equals(MenuManager.STATE_READY) || gameState.equals(MenuManager.STATE_PLAYING)) {
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                paddle.setDx(0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}