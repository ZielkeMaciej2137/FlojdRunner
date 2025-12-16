package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/* ===================== TILE ===================== */

enum TileKind { GROUND, BRICK, PIPE, SPIKE, GOAL }

class Block {
    final Rectangle rect;
    final TileKind kind;

    Block(Rectangle r, TileKind k) {
        rect = r;
        kind = k;
    }

    boolean solid() {
        return kind == TileKind.GROUND
                || kind == TileKind.BRICK
                || kind == TileKind.PIPE;
    }
}

/* ===================== LEVEL ===================== */

class Level1 {
    static final int TILE = 32;
    static final int W_TILES = 40;
    static final int H_TILES = 18;

    static final Point PLAYER_START = tileTopCenter(20, 13);

    static List<Block> build() {
        ArrayList<Block> L = new ArrayList<>();

        addRect(L, 0, 17, 40, 1, TileKind.GROUND);
        addRect(L, 2, 16, 2, 1, TileKind.GROUND);
        addRect(L, 6, 16, 3, 1, TileKind.GROUND);
        addRect(L, 11, 16, 2, 1, TileKind.GROUND);
        addRect(L, 24, 16, 3, 1, TileKind.GROUND);
        addRect(L, 33, 16, 2, 1, TileKind.GROUND);

        addRect(L, 0, 10, 1, 7, TileKind.PIPE);
        addRect(L, 4, 12, 1, 5, TileKind.PIPE);

        addRect(L, 10, 13, 4, 1, TileKind.BRICK);
        addRect(L, 3, 9, 2, 1, TileKind.BRICK);
        addRect(L, 6, 7, 3, 1, TileKind.BRICK);
        addRect(L, 9, 10, 4, 1, TileKind.BRICK);
        addRect(L, 10, 5, 5, 1, TileKind.BRICK);

        addRect(L, 19, 9, 1, 8, TileKind.PIPE);
        addRect(L, 20, 13, 2, 1, TileKind.BRICK);

        addRect(L, 16, 7, 2, 1, TileKind.BRICK);
        addRect(L, 16, 11, 3, 1, TileKind.BRICK);
        addRect(L, 24, 10, 3, 1, TileKind.BRICK);
        addRect(L, 22, 6, 1, 3, TileKind.PIPE);

        addRect(L, 25, 5, 6, 1, TileKind.BRICK);
        addRect(L, 29, 9, 2, 1, TileKind.BRICK);
        addRect(L, 32, 8, 1, 4, TileKind.PIPE);
        addRect(L, 35, 12, 2, 1, TileKind.BRICK);

        return L;
    }

    private static void addRect(List<Block> L, int tx, int ty, int tw, int th, TileKind k) {
        L.add(new Block(new Rectangle(tx*TILE, ty*TILE, tw*TILE, th*TILE), k));
    }

    private static Point tileTopCenter(int tx, int ty) {
        return new Point(tx*TILE + TILE/2, ty*TILE);
    }
}

/* ===================== ENTITY ===================== */

abstract class Entity {
    double x, y, vx, vy;
    final int w = 24, h = 32;

    boolean onGround;
    boolean onWallLeft, onWallRight;

    static final double MAX_RUN = 5.0;
    static final double ACCEL = 0.15;
    static final double AIR_CONTROL = 0.6;
    static final double GRAVITY = 0.65;

    static final double JUMP = -12.5;
    static final double WALL_JUMP_X = 6.5;

    Rectangle bounds() {
        return new Rectangle((int)Math.round(x), (int)Math.round(y), w, h);
    }

    void move(double dir) {
        double target = dir * MAX_RUN;
        double a = onGround ? ACCEL : ACCEL * AIR_CONTROL;
        vx += (target - vx) * a;
    }

    void physics(List<Block> blocks) {
        vy += GRAVITY;

        onWallLeft = onWallRight = false;

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
            }
        }

        y += vy;
        onGround = false;
        Rectangle ry = bounds();
        for (Block b : blocks) {
            if (!b.solid()) continue;
            if (ry.intersects(b.rect)) {
                if (vy > 0) {
                    y = b.rect.y - h;
                    onGround = true;
                } else {
                    y = b.rect.y + b.rect.height;
                }
                vy = 0;
            }
        }
    }

    void jump() {
        if (onGround) {
            vy = JUMP;
        } else if (onWallLeft) {
            vy = JUMP;
            vx = WALL_JUMP_X;
        } else if (onWallRight) {
            vy = JUMP;
            vx = -WALL_JUMP_X;
        }
    }
}

/* ===================== PLAYER ===================== */

class Player extends Entity {
    boolean left, right;
    boolean canDash = true;
    int dashTimer = 0;

    static final double DASH_SPEED = 12;
    static final int DASH_TIME = 10;
    static final int DASH_COOLDOWN = 30;
    int dashCooldown = 0;

    Player(Point p) {
        x = p.x;
        y = p.y - h;
    }

    void update(List<Block> blocks) {

        if (dashTimer > 0) {
            dashTimer--;
        } else {
            if (left) move(-1);
            if (right) move(1);
        }

        if (!left && !right && onGround)
            vx *= 0.8;

        if (dashCooldown > 0)
            dashCooldown--;

        physics(blocks);
    }

    void dash() {
        if (dashCooldown == 0) {
            dashTimer = DASH_TIME;
            dashCooldown = DASH_COOLDOWN;
            vx = right ? DASH_SPEED : left ? -DASH_SPEED : (vx >= 0 ? DASH_SPEED : -DASH_SPEED);
            vy = 0;
        }
    }

    void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect((int)x, (int)y, w, h);
    }
}

/* ===================== GHOST ===================== */

class Ghost extends Entity {

    Ghost(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void update(List<Block> blocks, Player p) {
        move(p.x < x ? -1 : 1);
        if ((onWallLeft || onWallRight || onGround) && p.y < y - 10)
            jump();
        physics(blocks);
    }

    void draw(Graphics2D g) {
        g.setColor(new Color(200, 200, 255));
        g.fillOval((int)x, (int)y, w, h);
    }
}

/* ===================== GAME PANEL ===================== */

class GamePanel extends JPanel {

    List<Block> blocks;
    Player player;
    Ghost ghost;
    boolean gameOver = false;

    double scaleX, scaleY;

    GamePanel() {
        resetGame();
        setFocusable(true);
        setBackground(new Color(135,206,235));

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        scaleX = s.getWidth() / (Level1.W_TILES * Level1.TILE);
        scaleY = s.getHeight() / (Level1.H_TILES * Level1.TILE);

        new Thread(() -> {
            final double STEP = 1.0 / 60.0;
            double acc = 0;
            long last = System.nanoTime();

            while (true) {
                long now = System.nanoTime();
                acc += (now - last) / 1e9;
                last = now;

                while (acc >= STEP) {
                    updateGame();
                    acc -= STEP;
                }
                repaint();
                try { Thread.sleep(2); } catch (Exception ignored) {}
            }
        }).start();

        bind("pressed A", () -> player.left = true);
        bind("released A", () -> player.left = false);
        bind("pressed D", () -> player.right = true);
        bind("released D", () -> player.right = false);
        bind("pressed W", player::jump);
        bind("pressed C", player::dash);

        bind("pressed ENTER", () -> {
            if (gameOver) resetGame();
        });
    }

    void resetGame() {
        blocks = Level1.build();
        player = new Player(Level1.PLAYER_START);
        ghost = new Ghost(Level1.W_TILES * Level1.TILE - 100,
                Level1.PLAYER_START.y - 32);
        gameOver = false;
    }

    void updateGame() {
        if (!gameOver) {
            player.update(blocks);
            ghost.update(blocks, player);
            if (player.bounds().intersects(ghost.bounds()))
                gameOver = true;
        }
    }

    void bind(String key, Runnable r) {
        getInputMap().put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(ActionEvent e) { r.run(); }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.scale(scaleX, scaleY);

        for (Block b : blocks) {
            g2.setColor(Color.DARK_GRAY);
            g2.fill(b.rect);
        }

        player.draw(g2);
        ghost.draw(g2);

        if (gameOver) {
            g2.setColor(new Color(0,0,0,180));
            g2.fillRect(0,0,
                    Level1.W_TILES*Level1.TILE,
                    Level1.H_TILES*Level1.TILE);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.drawString("GAME OVER",
                    Level1.W_TILES*Level1.TILE/2 - 150,
                    Level1.H_TILES*Level1.TILE/2);
        }
    }
}

/* ===================== MAIN ===================== */

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsDevice gd =
                    GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getDefaultScreenDevice();

            JFrame f = new JFrame();
            f.setUndecorated(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new GamePanel());
            gd.setFullScreenWindow(f);

            f.getRootPane().getInputMap()
                    .put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
            f.getRootPane().getActionMap()
                    .put("exit", new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
        });
    }
}
