package manager;

import object.brick.Brick;
import object.brick.NormalBrick;
import object.brick.StrongBrick;
import object.brick.VeryStrongBrick;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private int currentLevel;
    private int gameWidth;
    private int gameHeight;

    public LevelManager(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.currentLevel = 5;
    }

    public List<Brick> createBricksForCurrentLevel() {
        List<Brick> bricks = new ArrayList<>();
        List<String> levelMap = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/map/map.txt")))) {

            String line;
            boolean foundLevel = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.trim().equals("LEVEL" + currentLevel)) {
                    foundLevel = true;
                    continue;
                }

                if (foundLevel && line.trim().startsWith("LEVEL")) {
                    break;
                }

                if (foundLevel) {
                    levelMap.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return bricks;
        }

        if (levelMap.isEmpty()) return bricks;

        int spacing = 5;
        int brickWidth = 50;
        int brickHeight = 20;

        int maxCols = 0;
        for (String row : levelMap) {
            if (row.length() > maxCols) maxCols = row.length();
        }

        int totalMapWidth = maxCols * (brickWidth + spacing) - spacing;
        int startX = (gameWidth - totalMapWidth) / 2;
        int startY = 60;

        for (int row = 0; row < levelMap.size(); row++) {
            String rowStr = levelMap.get(row);

            for (int col = 0; col < rowStr.length(); col++) {
                char c = rowStr.charAt(col);
                if (c == ' ') continue;

                int x = startX + col * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);

                switch (c) {
                    case '1':
                        bricks.add(new NormalBrick(x, y, brickWidth, brickHeight, 1));
                        break;
                    case '2':
                        bricks.add(new StrongBrick(x, y, brickWidth, brickHeight, 2));
                        break;
                    case '3':
                        bricks.add(new VeryStrongBrick(x, y, brickWidth, brickHeight, 3));
                        break;
                }
            }
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