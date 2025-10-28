package manager;

import java.awt.*;
import game.GameManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

public class MenuManager {

    // --- Hằng số Trạng thái Game ---
    public static final String STATE_MENU = "menu";
    public static final String STATE_READY = "ready";
    public static final String STATE_PAUSED = "paused";
    public static final String STATE_PLAYING = "playing";
    public static final String STATE_GAME_OVER = "gameOver";
    public static final String STATE_GAME_WIN = "gameWin";

    // --- Hằng số Màn hình Menu ---
    public static final String SCREEN_MAIN = "main";
    public static final String SCREEN_PAUSE = "pause";
    public static final String SCREEN_OPTIONS = "options";
    public static final String SCREEN_CREDITS = "credits";
    public static final String SCREEN_LEVEL_SELECT = "levelSelect";

    // --- Hằng số Mục Menu Chính ---
    public static final int MAIN_START = 0;
    public static final int MAIN_LEVEL_SELECT = 1;
    public static final int MAIN_OPTIONS = 2;
    public static final int MAIN_CREDITS = 3;
    public static final int MAIN_EXIT = 4;
    public static final int MAIN_ITEMS_COUNT = 5;

    // --- Hằng số Level Select ---
    public static final int MAX_LEVEL = 10;
    public static final int LEVEL_BACK = MAX_LEVEL;
    public static final int LEVEL_ITEMS_COUNT = MAX_LEVEL + 1;

    // --- Hằng số Menu Tạm Dừng và Cài đặt ---
    public static final int PAUSE_RESUME = 0;
    public static final int PAUSE_RESTART = 1;
    public static final int PAUSE_MAIN_MENU = 2;
    public static final int PAUSE_ITEMS_COUNT = 3;

    public static final int OPTIONS_SOUND = 0;
    public static final int OPTIONS_LANGUAGE = 1;
    public static final int OPTIONS_BACK = 2;
    public static final int OPTIONS_ITEMS_COUNT = 3;

    // --- LOGIC ĐA NGÔN NGỮ ---
    private final Map<String, Map<String, String>> texts = new HashMap<>();

    public MenuManager() {
        // Khởi tạo các chuỗi tiếng Anh (EN)
        Map<String, String> en = new HashMap<>();
        en.put("GAME_TITLE", "JAVA BREAKOUT");
        en.put("READY_MSG", "PRESS SPACE TO LAUNCH BALL");
        en.put("OVER_MSG", "GAME OVER!");
        en.put("WIN_MSG", "YOU WIN!");
        en.put("SCORE_FINAL", "Final Score: ");
        en.put("RETURN_MENU", "PRESS ENTER FOR MENU");
        en.put("PAUSED", "PAUSED");
        en.put("CHOOSE_LEVEL", "CHOOSE LEVEL");
        en.put("CURRENT", " (Current: ");
        en.put("START_GAME", "START GAME");
        en.put("LEVEL_SELECT", "LEVEL SELECT");
        en.put("OPTIONS", "OPTIONS");
        en.put("CREDITS", "CREDITS");
        en.put("EXIT", "EXIT");
        en.put("SOUND", "SOUND");
        en.put("LANGUAGE", "LANGUAGE");
        en.put("BACK", "BACK");
        en.put("RESUME", "RESUME");
        en.put("RESTART_LEVEL", "RESTART LEVEL");
        en.put("MAIN_MENU", "MAIN MENU");
        en.put("CURRENT_LEVEL_SHORT", " (C)");
        en.put("CREDITS_TITLE", "ANOTHER UET LEGEND");
        en.put("CREDITS_LINE_1", "Three students awakened the 'DEADLINE BRICK' curse.");
        en.put("CREDITS_LINE_2", "They created ANKANOIRD to save the project.");
        en.put("CREDITS_LINE_3", "Break the bricks and submit on time!");
        en.put("CREDITS_LINE_4", "DEVELOPED BY: Nguyen Huu Kien - Nguyen Duc Thanh - Phan Huy Quang");

        texts.put("EN", en);

        // Khởi tạo các chuỗi tiếng Việt (VI)
        Map<String, String> vi = new HashMap<>();
        vi.put("GAME_TITLE", "GAME PHÁ GẠCH");
        vi.put("READY_MSG", "NHẤN SPACE ĐỂ BẮN BÓNG");
        vi.put("OVER_MSG", "TRÒ CHƠI KẾT THÚC!");
        vi.put("WIN_MSG", "BẠN THẮNG!");
        vi.put("SCORE_FINAL", "Điểm cuối: ");
        vi.put("RETURN_MENU", "NHẤN ENTER ĐỂ VỀ MENU");
        vi.put("PAUSED", "TẠM DỪNG");
        vi.put("CHOOSE_LEVEL", "CHỌN MÀN CHƠI");
        vi.put("CURRENT", " (Hiện tại: ");
        vi.put("START_GAME", "BẮT ĐẦU CHƠI");
        vi.put("LEVEL_SELECT", "CHỌN MÀN CHƠI");
        vi.put("OPTIONS", "CÀI ĐẶT");
        vi.put("CREDITS", "GIỚI THIỆU");
        vi.put("EXIT", "THOÁT GAME");
        vi.put("SOUND", "ÂM THANH");
        vi.put("LANGUAGE", "NGÔN NGỮ");
        vi.put("BACK", "QUAY LẠI");
        vi.put("RESUME", "TIẾP TỤC");
        vi.put("RESTART_LEVEL", "CHƠI LẠI MÀN");
        vi.put("MAIN_MENU", "MENU CHÍNH");
        vi.put("CURRENT_LEVEL_SHORT", " (C)");
        vi.put("CREDITS_TITLE", "TRUYỀN THUYẾT UET");
        vi.put("CREDITS_LINE_1", "Ba sinh viên đã đánh thức lời nguyền 'DEADLINE GẠCH ĐÁ'.");
        vi.put("CREDITS_LINE_2", "Họ tạo ra ANKANOIRD để giải cứu bài tập lớn.");
        vi.put("CREDITS_LINE_3", "Phá gạch cuối cùng, nộp bài đúng hạn!");
        vi.put("CREDITS_LINE_4", "PHÁT TRIỂN BỞI: Nguyễn Hữu Kiên - Nguyễn Đức Thành - Phan Huy Quang");

        texts.put("VI", vi);
    }

    public String getText(String key, String lang) {
        return texts.getOrDefault(lang, texts.get("EN")).getOrDefault(key, key);
    }

    public void drawMenuScreen(Graphics2D g2d, String gameState, String menuScreen, int score, int selectedItem, int currentLevel, boolean isMuted, String currentLanguage) {

        // Cài đặt chung
        int gameWidth = GameManager.GAME_WIDTH;
        int gameHeight = GameManager.GAME_HEIGHT;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        String lang = currentLanguage.toUpperCase();

        // ... [Logic vẽ READY/OVER/WIN giữ nguyên] ...

        if (gameState.equals(STATE_READY)) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(getText("READY_MSG", lang), gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("READY_MSG", lang)) / 2, gameHeight / 2);
            return;
        }

        if (gameState.equals(STATE_GAME_OVER)) {
            g2d.setColor(Color.RED);
            g2d.drawString(getText("OVER_MSG", lang), gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("OVER_MSG", lang)) / 2, gameHeight / 2);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.WHITE);
            g2d.drawString(getText("SCORE_FINAL", lang) + score, gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("SCORE_FINAL", lang) + score) / 2, gameHeight / 2 + 40);
            g2d.drawString(getText("RETURN_MENU", lang), gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("RETURN_MENU", lang)) / 2, gameHeight / 2 + 80);
            return;
        } else if (gameState.equals(STATE_GAME_WIN)) {
            g2d.setColor(Color.GREEN);
            g2d.drawString(getText("WIN_MSG", lang), gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("WIN_MSG", lang)) / 2, gameHeight / 2);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.WHITE);
            g2d.drawString(getText("SCORE_FINAL", lang) + score, gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("SCORE_FINAL", lang) + score) / 2, gameHeight / 2 + 40);
            g2d.drawString(getText("RETURN_MENU", lang), gameWidth / 2 - g2d.getFontMetrics().stringWidth(getText("RETURN_MENU", lang)) / 2, gameHeight / 2 + 80);
            return;
        }

        // --- CÁC MÀN HÌNH CÓ LIST MENU HOẶC MÀN HÌNH THÔNG TIN ---
        if (gameState.equals(STATE_MENU) || gameState.equals(STATE_PAUSED)) {

            String title = "";
            String[] menuItems = null;
            int itemsCount = 0;

            if (menuScreen.equals(SCREEN_MAIN)) {
                title = getText("GAME_TITLE", lang);
                menuItems = new String[]{
                        getText("START_GAME", lang),
                        getText("LEVEL_SELECT", lang),
                        getText("OPTIONS", lang),
                        getText("CREDITS", lang),
                        getText("EXIT", lang)
                };
                itemsCount = MAIN_ITEMS_COUNT;
            } else if (menuScreen.equals(SCREEN_LEVEL_SELECT)) {
                title = getText("CHOOSE_LEVEL", lang) + getText("CURRENT", lang) + currentLevel + ")";

                menuItems = new String[LEVEL_ITEMS_COUNT];
                for (int i = 0; i < MAX_LEVEL; i++) {
                    String status = (i + 1 == currentLevel) ? getText("CURRENT_LEVEL_SHORT", lang) : "";
                    menuItems[i] = "LEVEL " + (i + 1) + status;
                }
                menuItems[LEVEL_BACK] = getText("BACK", lang);
                itemsCount = LEVEL_ITEMS_COUNT;
            } else if (menuScreen.equals(SCREEN_PAUSE)) {
                title = getText("PAUSED", lang);
                menuItems = new String[]{
                        getText("RESUME", lang),
                        getText("RESTART_LEVEL", lang),
                        getText("MAIN_MENU", lang)
                };
                itemsCount = PAUSE_ITEMS_COUNT;
            } else if (menuScreen.equals(SCREEN_OPTIONS)) {
                title = getText("OPTIONS", lang);
                menuItems = new String[]{
                        getText("SOUND", lang) + ": " + (isMuted ? "OFF" : "ON"),
                        getText("LANGUAGE", lang) + ": " + currentLanguage,
                        getText("BACK", lang)
                };
                itemsCount = OPTIONS_ITEMS_COUNT;
            } else if (menuScreen.equals(SCREEN_CREDITS)) {
                // Xử lý Credits/Truyền thuyết UET Rút gọn
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                g2d.setColor(Color.YELLOW);

                String creditTitle = getText("CREDITS_TITLE", lang);
                g2d.drawString(creditTitle, gameWidth / 2 - g2d.getFontMetrics().stringWidth(creditTitle) / 2, 80);

                g2d.setFont(new Font("Arial", Font.PLAIN, 18));

                String[] legendLines = {
                        getText("CREDITS_LINE_1", lang),
                        getText("CREDITS_LINE_2", lang),
                        getText("CREDITS_LINE_3", lang),
                        "",
                        "PROJECT GAME: Arkanoird",
                        getText("CREDITS_LINE_4", lang),
                        "",
                        " ENTER TO RETURN"
                };

                int creditY = 130;
                int creditLineHeight = 30;

                for (String line : legendLines) {
                    if (line.contains("DEADLINE GẠCH ĐÁ") || line.contains("Ankanoird") || line.contains("ANKANOIRD") || line.contains("deadline") || line.contains("submit on time")) {
                        g2d.setColor(Color.ORANGE);
                    } else if (line.contains("Kien") || line.contains("Thanh") || line.contains("Quang")) {
                        g2d.setColor(Color.CYAN);
                    } else {
                        g2d.setColor(Color.WHITE);
                    }

                    g2d.drawString(line, gameWidth / 2 - g2d.getFontMetrics().stringWidth(line) / 2, creditY);
                    creditY += creditLineHeight;
                }
                return;
            } else {
                return;
            }

            // --- Logic vẽ Menu List chung ---
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.setColor(Color.YELLOW);
            g2d.drawString(title, gameWidth / 2 - g2d.getFontMetrics().stringWidth(title) / 2, gameHeight / 2 - 150);

            g2d.setFont(new Font("Arial", Font.BOLD, 22));

            // CÂN CHỈNH VỊ TRÍ
            int startY = gameHeight / 2 - 80;
            int lineHeight = 45; // Tăng nhẹ để có khoảng cách giữa các nút
            FontMetrics fm = g2d.getFontMetrics();

            // Vòng lặp vẽ Menu List (Chạy cho tất cả các Menu List)
            if (menuScreen.equals(SCREEN_LEVEL_SELECT)) {

                // --- LOGIC VẼ 2 CỘT CHO LEVEL SELECT VÀ VIỀN 3D ---
                int half = MAX_LEVEL / 2; // 5
                int col1X = gameWidth / 2 - 150;
                int col2X = gameWidth / 2 + 50;
                int rectWidth = 140;
                int rectHeight = fm.getHeight() + 10;

                for (int i = 0; i < LEVEL_ITEMS_COUNT; i++) {
                    String item = menuItems[i];
                    int x;
                    int itemY;
                    int finalRectWidth = rectWidth;

                    // Xác định vị trí X, Y và độ rộng cho nút BACK
                    if (i < half) {
                        x = col1X;
                        itemY = startY + i * lineHeight;
                    } else if (i < MAX_LEVEL) {
                        x = col2X;
                        itemY = startY + (i - half) * lineHeight;
                    } else { // Mục BACK (Luôn ở dưới cùng)
                        itemY = startY + half * lineHeight + 50;
                        finalRectWidth = 280; // Nút BACK full width chuẩn
                        x = gameWidth / 2 - finalRectWidth / 2;
                    }

                    int rectX = x;
                    int rectY = itemY - fm.getAscent() - 5;

                    // VẼ NÚT
                    if (i == selectedItem) {
                        g2d.setColor(new Color(255, 100, 100, 255)); // Màu nền highlight MÀU ĐẶC
                        g2d.fillRoundRect(rectX, rectY, finalRectWidth, rectHeight, 10, 10);

                        g2d.setColor(new Color(255, 255, 255)); // Viền ngoài (Highlight)
                        g2d.setStroke(new BasicStroke(2.5f));
                        g2d.drawRoundRect(rectX, rectY, finalRectWidth, rectHeight, 10, 10);

                        g2d.setColor(Color.BLACK); // Chữ đen
                    } else {
                        g2d.setColor(new Color(50, 50, 50, 200)); // Màu nền tối mờ
                        g2d.fillRoundRect(rectX, rectY, finalRectWidth, rectHeight, 10, 10);

                        g2d.setColor(new Color(150, 150, 150, 255)); // Viền ngoài (Shadow)
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.drawRoundRect(rectX, rectY, finalRectWidth, rectHeight, 10, 10);

                        g2d.setColor(Color.WHITE); // Chữ trắng
                    }

                    // VẼ CHỮ
                    int textX;
                    if (i < MAX_LEVEL) { // Căn giữa chữ trên nút Level 1-10
                        textX = rectX + (finalRectWidth - fm.stringWidth(item)) / 2;
                    } else { // Căn giữa chữ trên nút BACK
                        textX = rectX + (finalRectWidth - fm.stringWidth(item)) / 2;
                    }

                    g2d.drawString(item, textX, itemY);
                    g2d.setStroke(new BasicStroke(1)); // Đặt lại stroke
                }

            } else {
                // Logic vẽ Menu List một cột (MAIN, PAUSE, OPTIONS)
                final int BUTTON_STD_WIDTH = 280; // Chiều rộng chuẩn của nút

                for (int i = 0; i < itemsCount; i++) {
                    String item = menuItems[i];

                    int textWidth = fm.stringWidth(item);
                    int itemY = startY + i * lineHeight;

                    int rectWidth = BUTTON_STD_WIDTH;
                    int rectHeight = fm.getHeight() + 10;
                    int rectX = gameWidth / 2 - rectWidth / 2;
                    int rectY = itemY - fm.getAscent() - 5;

                    // VẼ NÚT
                    if (i == selectedItem) {
                        g2d.setColor(new Color(255, 100, 100, 255));
                        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10);

                        g2d.setColor(new Color(255, 255, 255));
                        g2d.setStroke(new BasicStroke(2.5f));
                        g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10);
                        g2d.setColor(Color.BLACK);
                    } else {
                        g2d.setColor(new Color(50, 50, 50, 200));
                        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10);
                        g2d.setColor(new Color(150, 150, 150, 255));
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10);
                        g2d.setColor(Color.WHITE);
                    }

                    // VẼ CHỮ
                    int textX = rectX + (rectWidth - textWidth) / 2;
                    g2d.drawString(item, textX, itemY);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
        }
    }
}