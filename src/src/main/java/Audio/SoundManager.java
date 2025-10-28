package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp SoundManager quản lý việc tải, lưu trữ và phát các clip âm thanh
 * trong trò chơi. Hỗ trợ tải từ hệ thống file (khi chạy trong IDE)
 * và từ bên trong file JAR (khi đóng gói game).
 */
public class SoundManager {
    // Map để lưu trữ các Clip âm thanh đã được tải
    private final Map<String, Clip> soundClips = new HashMap<>();

    /**
     * Tải một file âm thanh và lưu trữ nó dưới dạng Clip.
     * @param soundPath Đường dẫn tương đối đến file âm thanh (vd: "assets/sound/music.wav").
     */
    public void loadSound(String soundPath) {
        if (soundClips.containsKey(soundPath)) {
            return; // Đã tải, bỏ qua
        }

        Clip clip = null;
        AudioInputStream audioStream = null;

        try {
            // 1. Thử tải file từ bên trong file JAR (tài nguyên nội bộ)
            // Lấy URL tài nguyên. Đường dẫn phải bắt đầu bằng "/"
            String resourcePath = "/" + soundPath.replace('\\', '/');
            URL url = SoundManager.class.getResource(resourcePath);

            if (url != null) {
                // Tải thành công từ tài nguyên nội bộ (trong JAR)
                audioStream = AudioSystem.getAudioInputStream(url);
            } else {
                // 2. Thử tải file từ hệ thống file (khi chạy trong IDE)
                File soundFile = new File(soundPath);
                if (soundFile.exists()) {
                    audioStream = AudioSystem.getAudioInputStream(soundFile);
                } else {
                    System.err.println("Lỗi: Không tìm thấy file âm thanh ở cả hai nơi: " + soundPath);
                    return;
                }
            }

            // Mở Clip và lưu trữ
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundClips.put(soundPath, clip);
            System.out.println("Tải âm thanh thành công: " + soundPath);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Lỗi khi tải và mở âm thanh: " + soundPath);
            e.printStackTrace();
        } finally {
            // Đóng AudioInputStream sau khi hoàn thành
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Phát một âm thanh từ đầu. Nếu âm thanh chưa được tải, nó sẽ không làm gì.
     * @param soundPath Đường dẫn của âm thanh cần phát.
     * @param loop true nếu muốn lặp lại vô hạn (cho nhạc nền), false cho hiệu ứng âm thanh.
     */
    public void playSound(String soundPath, boolean loop) {
        Clip clip = soundClips.get(soundPath);
        if (clip == null) {
            System.err.println("Lỗi: Âm thanh chưa được tải: " + soundPath);
            return;
        }

        // Dừng và tua về đầu để có thể phát lại hiệu ứng âm thanh ngay lập tức
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp vô hạn
        } else {
            clip.start(); // Phát một lần
        }
    }

    /**
     * Dừng một âm thanh đang phát.
     * @param soundPath Đường dẫn của âm thanh cần dừng.
     */
    public void stopSound(String soundPath) {
        Clip clip = soundClips.get(soundPath);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    /**
     * Dọn dẹp tất cả các tài nguyên âm thanh đã mở.
     * Nên được gọi khi thoát game.
     */
    public void cleanup() {
        for (Clip clip : soundClips.values()) {
            clip.close();
        }
        soundClips.clear();
        System.out.println("Đã dọn dẹp các tài nguyên âm thanh.");
    }
}