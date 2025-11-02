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

        // 1. TÌM CHỈ SỐ CỘT BẮT ĐẦU CỦA KHỐI GẠCH DÀI NHẤT TRONG LEVEL NÀY
        int maxEffectiveCols = 0;
        int minGlobalIndex = Integer.MAX_VALUE;

        for (String row : rawLevelMap) {
            String trimmedRow = row.trim();

            // Tìm chiều dài hiệu quả của hàng (chỉ gạch + khoảng cách)
            if (trimmedRow.length() > maxEffectiveCols) {
                maxEffectiveCols = trimmedRow.length();
            }

            // Tìm chỉ số của ký tự không phải khoảng trắng ĐẦU TIÊN (lề trái)
            int firstNonSpace = row.indexOf(trimmedRow.length() > 0 ? trimmedRow.charAt(0) : ' ');
            if (firstNonSpace != -1) {
                if (firstNonSpace < minGlobalIndex) {
                    minGlobalIndex = firstNonSpace;
                }
            }
        }

        if (minGlobalIndex == Integer.MAX_VALUE) minGlobalIndex = 0; // Tránh lỗi nếu map toàn khoảng trắng

        // 2. TÍNH TOÁN VỊ TRÍ X BẮT ĐẦU VÀ BÙ TRỪ

        // Tổng chiều rộng bản đồ gạch
        int totalMapWidth = maxEffectiveCols * (brickWidth + spacing) - spacing;

        // Vị trí X TÍNH TOÁN ĐỂ CĂN GIỮA
        int startX_Centered = (gameWidth - 10 - totalMapWidth) / 2;

        // Vị trí X thực tế (Bù trừ lề trái của hàng gạch dài nhất)
        // Lỗi lệch phải xảy ra vì startX_Centered là vị trí ký tự đầu tiên,
        // nhưng nếu map có lề trái, ta cần bỏ qua lề đó.
        // Tuy nhiên, logic này phức tạp do file map có thể không đồng nhất.

        // ĐƠN GIẢN HÓA: Dùng startX_Centered và xóa trim()
        int startX = startX_Centered;

        int startY = 60;

        for (int row = 0; row < rawLevelMap.size(); row++) {
            String rowStr = rawLevelMap.get(row);

            // Bỏ qua khoảng trắng ở lề trái của hàng hiện tại
            int currentEffectiveCol = 0;
            int currentX = startX;

            for (int col = 0; col < rowStr.length(); col++) {
                char c = rowStr.charAt(col);

                if (c == ' ') {
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