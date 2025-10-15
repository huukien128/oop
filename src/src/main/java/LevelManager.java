import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevelManager {

    private int currentLevel;
    private int gameWidth;
    private int gameHeight;

    public LevelManager(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.currentLevel = 1;
    }

    public List<Brick> createBricksForCurrentLevel() {
        List<Brick> bricks = new ArrayList<>();
        List<String> levelMap = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/map/map.txt")))) {
            String line;
            boolean foundLevel = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("LEVEL" + currentLevel)) {
                    foundLevel = true;
                    continue;
                }

                if (foundLevel && line.trim().startsWith("LEVEL")) {
                    break;
                }

                if (foundLevel && !line.trim().isEmpty()) {
                    levelMap.add(line.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return bricks;
        }

        if (levelMap.isEmpty()) {
            return bricks;
        }

        int maxCols = 0;
        for (String row : levelMap) {
            if (row.length() > maxCols) {
                maxCols = row.length();
            }
        }

        // Tăng giá trị padding để tạo lề lớn hơn
        int padding = 20;
        int spacing = 5;

        int availableWidth = gameWidth - (2 * padding) - ((maxCols - 1) * spacing);
        int brickWidth = availableWidth / maxCols;
        int brickHeight = 20;
        int rowCount = 0;

        for (String row : levelMap) {
            int colCount = 0;
            for (char brickType : row.toCharArray()) {
                int brickX = padding + colCount * (brickWidth + spacing);
                int brickY = 50 + rowCount * (brickHeight + spacing);

                switch (brickType) {
                    case '1':
                        bricks.add(new NormalBrick(brickX, brickY, brickWidth, brickHeight, 1));
                        break;
                    case '2':
                        bricks.add(new StrongBrick(brickX, brickY, brickWidth, brickHeight, 2));
                        break;
                    case '3':
                        bricks.add(new VeryStrongBrick(brickX, brickY, brickWidth, brickHeight, 3));
                        break;
                    case ' ':
                        break;
                }
                colCount++;
            }
            rowCount++;
        }
        return bricks;
    }

    public void nextLevel() {
        currentLevel++;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}