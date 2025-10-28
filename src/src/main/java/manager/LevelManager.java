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

    public LevelManager(int gameWidth, int integer) {
        this.gameWidth = gameWidth;
        this.gameHeight = integer;
        this.currentLevel = 1;
    }

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

        // TÍNH TOÁN KÍCH THƯỚC VÀ VỊ TRÍ
        int spacing = 4;
        int brickWidth = 60;
        int brickHeight = 20;

        // 1. Tìm chiều dài hàng gạch HIỆU QUẢ dài nhất
        int maxEffectiveCols = 0;
        for (String row : rawLevelMap) {
            // Chỉ cần lấy chiều dài của chuỗi sau khi trim (loại bỏ khoảng trắng thừa ở hai đầu)
            if (row.trim().length() > maxEffectiveCols) {
                maxEffectiveCols = row.trim().length();
            }
        }

        // 2. Tính toán tổng chiều rộng bản đồ GẠCH HIỆU QUẢ
        // Chiều rộng = Số gạch tối đa * (Chiều rộng gạch + Khoảng cách) - Khoảng cách cuối cùng
        int totalMapWidth = maxEffectiveCols * (brickWidth + spacing) - spacing;

        // 3. CÔNG THỨC CÂN CHỈNH CHÍNH XÁC
        int startX = (gameWidth - 10 - totalMapWidth) / 2;

        int startY = 60;

        for (int row = 0; row < rawLevelMap.size(); row++) {
            String rowStr = rawLevelMap.get(row);

            // Tìm chỉ mục bắt đầu của hàng gạch hiệu quả (chỉ số của ký tự gạch đầu tiên sau khoảng trắng)
            int effectiveStartIndex = 0;
            while(effectiveStartIndex < rowStr.length() && rowStr.charAt(effectiveStartIndex) == ' ') {
                effectiveStartIndex++;
            }

            // Bỏ qua nếu hàng này toàn khoảng trắng
            if (effectiveStartIndex == rowStr.length()) continue;

            // Vị trí cột gạch hiện tại (chỉ tính ký tự gạch và khoảng cách giữa gạch)
            int currentEffectiveCol = 0;

            for (int col = effectiveStartIndex; col < rowStr.length(); col++) {
                char c = rowStr.charAt(col);

                if (c == ' ') {
                    // Nếu gặp khoảng trắng giữa gạch, chúng ta vẫn tính nó là một "cột" (khoảng trống giữa gạch)
                    currentEffectiveCol++;
                    continue;
                }

                // Vị trí X: startX đã căn giữa + vị trí cột hiện tại * (chiều rộng + khoảng cách)
                int x = startX + currentEffectiveCol * (brickWidth + spacing);
                int y = startY + row * (brickHeight + spacing);

                // Khởi tạo gạch
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

                // Tăng cột sau khi vẽ gạch
                currentEffectiveCol++;
            }
        }

        return bricks;
    }

    public void nextLevel() {
        if (currentLevel < 10) {
            currentLevel++;
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int levelToSet) {
        if (levelToSet >= 1 && levelToSet <= 10) {
            this.currentLevel = levelToSet;
        } else {
            System.err.println("Lỗi: Level không hợp lệ (Phải từ 1 đến 10). Đặt mặc định là 1.");
            this.currentLevel = 1;
        }
    }
}