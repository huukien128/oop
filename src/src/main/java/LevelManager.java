import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private int currentLevel;
    private int gameWidth;
    private int gameHeight;

    public LevelManager(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.currentLevel = 1; // Bắt đầu từ level 1
    }

    public List<Brick> createBricksForCurrentLevel() {
        List<Brick> bricks = new ArrayList<>();
        switch (currentLevel) {
            case 1:
                createLevel1(bricks);
                break;
            case 2:
                createLevel2(bricks);
                break;
            case 3:
                createLevel3(bricks);
                break;
            default:
                // Mặc định hoặc level cuối cùng
                break;
        }
        return bricks;
    }

    private void createLevel1(List<Brick> bricks) {
        // Level 1: Dễ, nhiều gạch thường
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                bricks.add(new NormalBrick(j * 80 + 35, i * 30 + 50, 70, 20, 1));
            }
        }
    }

    private void createLevel2(List<Brick> bricks) {
        // Level 2: Khó hơn, thêm gạch mạnh
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (i % 2 == 0) {
                    bricks.add(new NormalBrick(j * 80 + 35, i * 30 + 50, 70, 20, 1));
                } else {
                    bricks.add(new StrongBrick(j * 80 + 35, i * 30 + 50, 70, 20, 2));
                }
            }
        }
    }

    private void createLevel3(List<Brick> bricks) {
        // Level 3: Khó nhất, gạch rất mạnh và bố cục phức tạp hơn
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (i % 3 == 0) {
                    bricks.add(new VeryStrongBrick(j * 80 + 35, i * 30 + 50, 70, 20, 3));
                } else if (i % 3 == 1) {
                    bricks.add(new StrongBrick(j * 80 + 35, i * 30 + 50, 70, 20, 2));
                } else {
                    bricks.add(new NormalBrick(j * 80 + 35, i * 30 + 50, 70, 20, 1));
                }
            }
        }
    }

    public void nextLevel() {
        currentLevel++;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}