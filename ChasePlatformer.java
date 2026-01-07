import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/* ===================== TILE / LEVEL ===================== */

enum TileKind { GROUND, BRICK, PIPE }

class Block {
    final Rectangle rect;
    final TileKind kind;

    Block(Rectangle r, TileKind k) {
        rect = r;
        kind = k;
    }

    boolean solid() {
        return kind == TileKind.GROUND || kind == TileKind.BRICK || kind == TileKind.PIPE;
    }
}

class Level1 {
    static final int TILE = 32;
    static final int W_TILES = 40;
    static final int H_TILES = 18;

    static final Point PLAYER_START = tileTopCenter(6, 13);
    static final Point POLICE_START = tileTopCenter(34, 13);

    static List<Block> build() {
        ArrayList<Block> L = new ArrayList<>();

        // ziemia (pełna)
        addRect(L, 0, 17, 40, 1, TileKind.GROUND);

        // 2 krótkie platformy przy starcie i po prawej
        addRect(L, 4, 16, 4, 1, TileKind.GROUND);
        addRect(L, 30, 16, 4, 1, TileKind.GROUND);

//        // 3 ściany do wall-jump (proste "wieże")
//        addRect(L, 12, 12, 1, 5, TileKind.PIPE);
//        addRect(L, 20, 10, 1, 7, TileKind.PIPE);
//        addRect(L, 28, 11, 1, 6, TileKind.PIPE);

        // 2 platformy w powietrzu (przeskakiwanie)
        addRect(L, 14, 13, 4, 1, TileKind.BRICK);
        addRect(L, 22, 8, 4, 1, TileKind.BRICK);

        // 1 dłuższa platforma końcowa (żeby było gdzie uciec)
        addRect(L, 32, 12, 6, 1, TileKind.BRICK);

        return L;
    }

    private static void addRect(List<Block> L, int tx, int ty, int tw, int th, TileKind k) {
        L.add(new Block(new Rectangle(tx * TILE, ty * TILE, tw * TILE, th * TILE), k));
    }

    private static Point tileTopCenter(int tx, int ty) {
        return new Point(tx * TILE + TILE / 2, ty * TILE);
    }
}

/* ===================== ENTITY ===================== */

abstract class Entity {
    double x, y, vx, vy;
    final int w = 28, h = 38;

    boolean onGround;
    boolean onWallLeft, onWallRight;

    // Physics
    static final double GRAVITY = 0.65;
    static final double JUMP_VY = -12.5;

    // Movement params (override in subclasses)
    double maxRun() { return 5.0; }
    double accel()  { return 0.18; }
    double airControl() { return 0.65; }

    Rectangle bounds() {
        return new Rectangle((int)Math.round(x), (int)Math.round(y), w, h);
    }

    void move(double dir) {
        double target = dir * maxRun();
        double a = onGround ? accel() : accel() * airControl();
        vx += (target - vx) * a;
    }

    void applyFrictionIfIdle(boolean left, boolean right) {
        if (!left && !right && onGround) vx *= 0.78;
    }

    void physics(List<Block> blocks) {
        // gravity
        vy += GRAVITY;

        // reset wall flags each tick; will be set by collision
        onWallLeft = onWallRight = false;

        // X axis
        x += vx;
        Rectangle rx = bounds();
        for (Block b : blocks) {
            if (!b.solid()) continue;
            if (rx.intersects(b.rect)) {
                if (vx > 0) {
                    x = b.rect.x - w;
                    onWallRight = true;
                } else if (vx < 0) {
                    x = b.rect.x + b.rect.width;
                    onWallLeft = true;
                }
                vx = 0;
                rx = bounds();
            }
        }

        // Y axis
        y += vy;
        onGround = false;
        Rectangle ry = bounds();
        for (Block b : blocks) {
            if (!b.solid()) continue;
            if (ry.intersects(b.rect)) {
                if (vy > 0) {
                    y = b.rect.y - h;
                    onGround = true;
                } else if (vy < 0) {
                    y = b.rect.y + b.rect.height;
                }
                vy = 0;
                ry = bounds();
            }
        }
    }

    // default jump = only ground jump (player overrides wall jump)
    void jump() {
        if (onGround) {
            vy = JUMP_VY;
        }
    }

    abstract void draw(Graphics2D g);
}

/* ===================== PLAYER (wall jump + slide) ===================== */

class Player extends Entity {
    boolean left, right;

    // Wall-jump / slide settings
    static final double WALL_JUMP_X = 7.2;
    static final double WALL_SLIDE_MAX_VY = 2.0;   // im mniejsze, tym wolniejszy zjazd
    static final double WALL_STICK_MIN_VX = 0.6;   // drobna „lepkość” na ścianie

    // logika: po złapaniu ściany w powietrzu dajemy "jedno odbicie" dopóki nie oderwie się od ściany
    boolean wallJumpAvailable = false;

    Player(Point start) {
        x = start.x;
        y = start.y - h;
    }

    @Override double maxRun() { return 5.6; }   // gracz szybciej
    @Override double accel()  { return 0.20; }

    void update(List<Block> blocks) {
        if (left)  move(-1);
        if (right) move(1);
        applyFrictionIfIdle(left, right);

        // physics step
        physics(blocks);

        // wall slide (TYLKO gracz)
        boolean onWall = (onWallLeft || onWallRight);
        if (!onGround && onWall && vy > 0) {
            // ogranicz prędkość spadania
            if (vy > WALL_SLIDE_MAX_VY) vy = WALL_SLIDE_MAX_VY;

            // minimalny docisk do ściany, żeby nie "odklejało" od micro drgań
            if (onWallLeft && vx < WALL_STICK_MIN_VX)  vx = Math.max(vx, 0);
            if (onWallRight && vx > -WALL_STICK_MIN_VX) vx = Math.min(vx, 0);
        }

        // uzbrajanie wall-jump: tylko gdy w powietrzu złapie ścianę
        if (!onGround && onWall) {
            // jeśli dopiero co dotknął ściany, uzbrój skok od ściany
            if (!wallJumpAvailable) wallJumpAvailable = true;
        } else {
            // jeśli nie jest na ścianie (albo wylądował), reset
            wallJumpAvailable = false;
        }
    }

    @Override
    void jump() {
        // normalny skok
        if (onGround) {
            vy = JUMP_VY;
            return;
        }

        // wall jump: tylko gdy jest na ścianie w powietrzu i "dodatkowy" skok jest dostępny
        if (wallJumpAvailable) {
            if (onWallLeft) {
                vy = JUMP_VY;
                vx = WALL_JUMP_X;
                wallJumpAvailable = false;
            } else if (onWallRight) {
                vy = JUMP_VY;
                vx = -WALL_JUMP_X;
                wallJumpAvailable = false;
            }
        }
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(new Color(80, 180, 255));
        g.fillRoundRect((int)x, (int)y, w, h, 8, 8);
    }
}

/* ===================== POLICE (slower, no wall jump) ===================== */

class Police extends Entity {
    Police(Point start) {
        x = start.x;
        y = start.y - h;
    }

    @Override double maxRun() { return 4.0; }  // wolniejszy
    @Override double accel()  { return 0.15; }
    @Override double airControl() { return 0.55; }

    void update(List<Block> blocks, Player p) {
        // proste AI: gonienie po X
        if (p.x < x - 2) move(-1);
        else if (p.x > x + 2) move(1);

        applyFrictionIfIdle(false, false);

        // skok tylko z ziemi (bez odbić od ścian)
        // skacz, jeśli gracz jest wyżej i policjant stoi na ziemi
        if (onGround && p.y + p.h < y - 10) {
            jump();
        }

        physics(blocks);
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(new Color(255, 80, 80));
        g.fillRoundRect((int)x, (int)y, w, h, 8, 8);
    }
}

/* ===================== GAME PANEL ===================== */

class GamePanel extends JPanel {
    final int worldW = Level1.W_TILES * Level1.TILE;
    final int worldH = Level1.H_TILES * Level1.TILE;

    List<Block> blocks;
    Player player;
    Police police;

    boolean gameOver = false;

    // camera / scale (prosto: dopasuj do okna)
    double scaleX = 1.0, scaleY = 1.0;

    final Timer timer;

    GamePanel() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(30, 30, 40));
        setFocusable(true);

        resetGame();

        // Input via key bindings (stabilniejsze niż KeyListener)
        bind("pressed A", () -> player.left = true);
        bind("released A", () -> player.left = false);
        bind("pressed D", () -> player.right = true);
        bind("released D", () -> player.right = false);

        bind("pressed LEFT", () -> player.left = true);
        bind("released LEFT", () -> player.left = false);
        bind("pressed RIGHT", () -> player.right = true);
        bind("released RIGHT", () -> player.right = false);

        bind("pressed SPACE", () -> player.jump());
        bind("pressed W", () -> player.jump());
        bind("pressed UP", () -> player.jump());


        bind("pressed R", () -> { if (gameOver) resetGame(); });

        timer = new Timer(16, e -> {
            updateGame();
            repaint();
        });
        timer.start();
    }

    void resetGame() {
        blocks = Level1.build();
        player = new Player(Level1.PLAYER_START);
        police = new Police(Level1.POLICE_START);
        gameOver = false;

        player.left = false;
        player.right = false;
    }


    void updateGame() {
        if (gameOver) return;

        player.update(blocks);
        police.update(blocks, player);

        if (player.bounds().intersects(police.bounds())) {
            gameOver = true;
        }
    }

    void bind(String key, Runnable r) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(ActionEvent e) { r.run(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // skalowanie świata do okna
        scaleX = getWidth() / (double) worldW;
        scaleY = getHeight() / (double) worldH;
        g2.scale(scaleX, scaleY);

        // tło świata
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, worldW, worldH);

        // bloczki
        for (Block b : blocks) {
            if (b.kind == TileKind.GROUND) g2.setColor(new Color(70, 70, 90));
            else if (b.kind == TileKind.BRICK) g2.setColor(new Color(90, 90, 110));
            else g2.setColor(new Color(60, 130, 60)); // PIPE jako "ściany"
            g2.fill(b.rect);
        }

        // entity
        player.draw(g2);
        police.draw(g2);

        // HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Sterowanie: A/D lub ←/→ ruch, SPACE/W/↑ skok. WALL JUMP: w powietrzu na ścianie + SPACE.", 18, 24);
        g2.drawString("Zjazd po ścianie: jeśli nie odbijesz się, postać zsuwa się wolno. Policjant: wolniejszy, bez wall jump.", 18, 44);
        g2.drawString("RESET po przegranej: R", 18, 64);

        if (gameOver) {
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(0, 0, worldW, worldH);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 52));
            String txt = "GAME OVER";
            int tw = g2.getFontMetrics().stringWidth(txt);
            g2.drawString(txt, (worldW - tw) / 2, worldH / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 22));
            String sub = "Naciśnij R aby zrestartować";
            int sw = g2.getFontMetrics().stringWidth(sub);
            g2.drawString(sub, (worldW - sw) / 2, worldH / 2 + 40);
        }

        g2.dispose();
    }
}

/* ===================== MAIN ===================== */

public class ChasePlatformer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Chase Platformer (Player vs Police) - Wall Jump");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(true);

            GamePanel panel = new GamePanel();
            f.add(panel);

            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
