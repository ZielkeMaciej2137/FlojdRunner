package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

enum TileKind {
    GROUND, BRICK, PIPE, SPIKE, GOAL
}

class Block {
    final Rectangle rect;
    final TileKind kind;

    Block(Rectangle rect, TileKind kind) {
        this.rect = rect;
        this.kind = kind;
    }
}

class Level1 {
    public static final int TILE = 32;
    public static final int W_TILES = 40;
    public static final int H_TILES = 18;
    public static final Point PLAYER_START_PX = tileTopCenter(20, 13);

    public static List<Block> build() {
        java.util.ArrayList<Block> L = new java.util.ArrayList<>();

        addRect(L, 0, 17, 40, 1, TileKind.GROUND);
        addRect(L, 2, 16, 2, 1, TileKind.GROUND);
        addRect(L, 6, 16, 3, 1, TileKind.GROUND);
        addRect(L, 11, 16, 2, 1, TileKind.GROUND);
        addRect(L, 27, 16, 3, 1, TileKind.GROUND);
        addRect(L, 33, 16, 2, 1, TileKind.GROUND);

        addRect(L, 1, 10, 1, 7, TileKind.PIPE);
        addRect(L, 4, 12, 1, 5, TileKind.PIPE);

        addRect(L, 3, 9, 2, 1, TileKind.BRICK);
        addRect(L, 6, 7, 3, 1, TileKind.BRICK);
        addRect(L, 9, 10, 4, 1, TileKind.BRICK);

        addRect(L, 19, 9, 1, 8, TileKind.PIPE);
        addRect(L, 20, 13, 2, 1, TileKind.BRICK);

        addRect(L, 16, 11, 3, 1, TileKind.BRICK);
        addRect(L, 23, 10, 3, 1, TileKind.BRICK);
        addRect(L, 21, 6, 1, 3, TileKind.PIPE);

        addRect(L, 24, 5, 6, 1, TileKind.BRICK);
        addRect(L, 29, 9, 2, 1, TileKind.BRICK);
        addRect(L, 32, 8, 1, 4, TileKind.PIPE);
        addRect(L, 34, 12, 4, 1, TileKind.BRICK);

        addRect(L, 15, 16, 1, 1, TileKind.SPIKE);
        addRect(L, 30, 16, 1, 1, TileKind.SPIKE);

        addRect(L, 38, 6, 1, 11, TileKind.GOAL);

        return L;
    }

    private static void addRect(List<Block> L, int tx, int ty, int tw, int th, TileKind kind) {
        Rectangle r = new Rectangle(tx * TILE, ty * TILE, tw * TILE, th * TILE);
        L.add(new Block(r, kind));
    }

    private static Point tileTopCenter(int tx, int ty) {
        int x = tx * TILE + TILE / 2;
        int y = ty * TILE;
        return new Point(x, y);
    }
}

// --------------------------
// PANEL GRY
// --------------------------
class GamePanel extends JPanel {

    private final List<Block> blocks;
    private final Point playerStart;
    private double scaleX, scaleY;

    public GamePanel(Dimension screenSize) {
        this.blocks = Level1.build();
        this.playerStart = Level1.PLAYER_START_PX;
        setBackground(new Color(135, 206, 235));

        // skalowanie kafelków do rozmiaru ekranu
        int baseWidth = Level1.W_TILES * Level1.TILE;
        int baseHeight = Level1.H_TILES * Level1.TILE;
        scaleX = screenSize.getWidth() / baseWidth;
        scaleY = screenSize.getHeight() / baseHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.scale(scaleX, scaleY);

        for (Block b : blocks) {
            switch (b.kind) {
                case GROUND -> g2.setColor(new Color(139, 69, 19));
                case BRICK -> g2.setColor(new Color(205, 133, 63));
                case PIPE -> g2.setColor(Color.GREEN.darker());
                case SPIKE -> g2.setColor(Color.RED);
                case GOAL -> g2.setColor(Color.YELLOW);
            }
            g2.fillRect(b.rect.x, b.rect.y, b.rect.width, b.rect.height);
            g2.setColor(Color.BLACK);
            g2.drawRect(b.rect.x, b.rect.y, b.rect.width, b.rect.height);
        }

        // punkt startowy gracza
        g2.setColor(Color.BLUE);
        g2.fillOval(playerStart.x - 6, playerStart.y - 12, 12, 12);
    }
}

// --------------------------
// OKNO PEŁNOEKRANOWE
// --------------------------
public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Pobierz rozmiar ekranu
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            JFrame frame = new JFrame("Full Screen Mario Map");
            frame.setUndecorated(true); // bez ramek
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GamePanel panel = new GamePanel(screenSize);
            frame.add(panel);
            gd.setFullScreenWindow(frame);

            // Wyjście z gry po ESC
            panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
            panel.getActionMap().put("exit", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gd.setFullScreenWindow(null);
                    System.exit(0);
                }
            });
        });
    }
}