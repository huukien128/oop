import javax.swing.*;

public class gameView {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout Game");
        GameManager gameManager = new GameManager();
        frame.add(gameManager);
        frame.setSize(GameManager.GAME_WIDTH, GameManager.GAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        gameManager.startGame();
    }
}