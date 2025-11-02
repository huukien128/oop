package manager;

import object.brick.Brick;
import object.brick.NormalBrick;
import object.brick.StrongBrick;
import object.brick.VeryStrongBrick;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp LevelManager quản lý việc tải và xây dựng cấu trúc gạch (bricks)
 * cho mỗi cấp độ từ file map.txt, đồng thời quản lý cấp độ hiện tại của trò chơi.
 */
public class LevelManager {

    private int currentLevel;
    private int gameWidth;
    private int gameHeight;

    /**
     * Khởi tạo LevelManager với kích thước màn hình game.
     * Cấp độ ban đầu được đặt là 1.
     * @param gameWidth Chiều rộng của khu vực chơi.
     * @param gameHeight Chiều cao của khu vực chơi.
     */
    public LevelManager(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.currentLevel = 1;
    }

    /**
     * Tạo danh sách gạch (Brick) dựa trên cấu trúc của cấp độ hiện tại.
     * Phương thức này đọc file map.txt, tìm đến phần LEVELx tương ứng,
     * và tính toán vị trí, loại gạch để căn giữa trên màn hình.
     * @return Danh sách các đối tượng Brick cho cấp độ hiện tại.
     */
    public List<Brick> createBricksForCurrentLevel() {
        List<Brick> bricks = new ArrayList<>();
        List<String> rawLevelMap = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/map/map.txt")))) {

            String line;
            boolean foundLevel = false;
            int levelToLoad = Math.min(currentLevel, 10);

            while ((line = br.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) continue;

                if (trimmedLine.equals("LEVEL" + levelToLoad)) {
                    foundLevel = true;
                    continue;
                }

                if (foundLevel && trimmedLine.startsWith("LEVEL")) {
                    break;
                }

                if (foundLevel) {
                    rawLevelMap.add(line);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tải map level " + currentLevel);
            e.printStackTrace();
            return bricks;
        }

        if (rawLevelMap.isEmpty()) {
            System.err.println("Không tìm thấy dữ liệu map cho LEVEL" + currentLevel + " trong file.");
            return bricks;
        }

        int spacing = 4;
        int brickWidth = 60;
        int brickHeight = 20;

        int maxEffectiveCols = 0;
        int minGlobalIndex = Integer.MAX_VALUE;

        for (String row : rawLevelMap) {
            String trimmedRow = row.trim();

            if (trimmedRow.length() > maxEffectiveCols) {
                maxEffectiveCols = trimmedRow.length();
            }

            int firstNonSpace = row.indexOf(trimmedRow.length() > 0 ? trimmedRow.charAt(0) : ' ');
            if (firstNonSpace != -1) {
                if (firstNonSpace < minGlobalIndex) {
                    minGlobalIndex = firstNonSpace;
                }
            }
        }

        if (minGlobalIndex == Integer.MAX_VALUE) minGlobalIndex = 0;

        int totalMapWidth = maxEffectiveCols * (brickWidth + spacing) - spacing;

        int startX_Centered = (gameWidth - 10 - totalMapWidth) / 2;

        int startX = startX_Centered;

        int startY = 60;

        for (int row = 0; row < rawLevelMap.size(); row++) {
            String rowStr = rawLevelMap.get(row);

            int currentEffectiveCol = 0;

            for (int col = 0; col < rowStr.length(); col++) {
                char c = rowStr.charAt(col);

                if (c == ' ') {
                    currentEffectiveCol++;
                    continue;
                }

                int x = startX + currentEffectiveCol * (brickWidth + spacing);
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

                currentEffectiveCol++;
            }
        }

        return bricks;
    }

    /**
     * Tăng cấp độ hiện tại lên 1, tối đa là level 10.
     */
    public void nextLevel() {
        if (currentLevel < 10) {
            currentLevel++;
        }
    }

    /**
     * Lấy cấp độ hiện tại của trò chơi.
     * @return Cấp độ hiện tại (từ 1 đến 10).
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Đặt cấp độ hiện tại của trò chơi.
     * @param levelToSet Cấp độ muốn đặt (phải từ 1 đến 10).
     */
    public void setCurrentLevel(int levelToSet) {
        if (levelToSet >= 1 && levelToSet <= 10) {
            this.currentLevel = levelToSet;
        } else {
            System.err.println("Lỗi: Level không hợp lệ (Phải từ 1 đến 10). Đặt mặc định là 1.");
            this.currentLevel = 1;
        }
    }
}