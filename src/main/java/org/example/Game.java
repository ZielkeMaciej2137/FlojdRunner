package org.example;
import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    private static final int TILE_SIZE = 32;
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int COLS = SCREEN_WIDTH / TILE_SIZE;
    private static final int ROWS = SCREEN_HEIGHT / TILE_SIZE;

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Tło nieba
        g.setColor(new Color(135, 206, 235)); // jasnoniebieski
        g.fillRect(0, 0, getWidth(), getHeight());

        // Rysowanie brązowej podłogi na dole
        for (int col = 0; col < COLS; col++) {
            g.setColor(new Color(139, 69, 19)); // brązowy
            g.fillRect(col * TILE_SIZE, SCREEN_HEIGHT - TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(col * TILE_SIZE, SCREEN_HEIGHT - TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Rysowanie przeszkody (kamień) na podłodze
        int stoneX = SCREEN_WIDTH / 2;
        int stoneY = SCREEN_HEIGHT - TILE_SIZE * 2;
        g.setColor(Color.GRAY);
        g.fillRect(stoneX, stoneY, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(stoneX, stoneY, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Retro Gra 2D");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true); // pełnoekranowe bez ramki
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // pełny ekran
            frame.add(new Game());
            frame.setVisible(true);
        });
    }
}
