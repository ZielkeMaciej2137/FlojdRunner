import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChaseGame extends JPanel implements ActionListener, KeyListener {

    // Rozmiar okna
    private static final int WIDTH = 800;
    private static final int HEIGHT = 450;

    // Podłoga
    private static final int GROUND_Y = 360;

    // Parametry graczy
    private static final int PLAYER_WIDTH = 40;
    private static final int PLAYER_HEIGHT = 60;
    private static final int POLICE_WIDTH = 40;
    private static final int POLICE_HEIGHT = 60;

    // Ruch poziomy
    private static final int PLAYER_SPEED = 6;
    private static final int POLICE_SPEED = 4;

    // Fizyka skoku
    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -12.0;

    // Pozycje i prędkości
    private double playerX = 100;
    private double playerY = GROUND_Y - PLAYER_HEIGHT;
    private double playerVX = 0;
    private double playerVY = 0;

    private double policeX = 600;
    private double policeY = GROUND_Y - POLICE_HEIGHT;
    private double policeVX = 0;
    private double policeVY = 0;

    // Skok / double jump
    private int playerJumpCount = 0; // 0, 1, 2
    private boolean playerOnGround = true;
    private boolean policeOnGround = true;

    // Sterowanie
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Timer gry
    private Timer timer;
    private boolean gameOver = false;

    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(30, 30, 40));

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        // ~60 FPS
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updatePlayer();
            //updatePolice();
            //checkCollision();
        }
        repaint();
    }

    private void updatePlayer() {
        // Poziom
        playerVX = 0;
        if (leftPressed) {
            playerVX = -PLAYER_SPEED;
        }
        if (rightPressed) {
            playerVX = PLAYER_SPEED;
        }

        playerX += playerVX;
        if (playerX < 0) playerX = 0;
        if (playerX > WIDTH - PLAYER_WIDTH) playerX = WIDTH - PLAYER_WIDTH;

        // Grawitacja i skok
        playerVY += GRAVITY;
        playerY += playerVY;

        // Podłoga
        if (playerY >= GROUND_Y - PLAYER_HEIGHT) {
            playerY = GROUND_Y - PLAYER_HEIGHT;
            playerVY = 0;
            if (!playerOnGround) {
                playerOnGround = true;
                playerJumpCount = 0; // reset double jump
            }
        } else {
            playerOnGround = false;
        }
    }

    private void updatePolice() {
        // AI: gonienie gracza w poziomie
        if (playerX < policeX - 1) {
            policeVX = -POLICE_SPEED;
        } else if (playerX > policeX + 1) {
            policeVX = POLICE_SPEED;
        } else {
            policeVX = 0;
        }

        policeX += policeVX;
        if (policeX < 0) policeX = 0;
        if (policeX > WIDTH - POLICE_WIDTH) policeX = WIDTH - POLICE_WIDTH;

        // Prosta logika skoku:
        // Jeśli gracz jest wyżej, a policjant stoi na ziemi – policjant skacze.
        if (playerY + PLAYER_HEIGHT < policeY + POLICE_HEIGHT - 10 && policeOnGround) {
            policeVY = JUMP_STRENGTH;
            policeOnGround = false;
        }

        // Grawitacja
        policeVY += GRAVITY;
        policeY += policeVY;

        // Podłoga
        if (policeY >= GROUND_Y - POLICE_HEIGHT) {
            policeY = GROUND_Y - POLICE_HEIGHT;
            policeVY = 0;
            policeOnGround = true;
        }
    }

    private void checkCollision() {
        Rectangle playerRect = new Rectangle((int) playerX, (int) playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
        Rectangle policeRect = new Rectangle((int) policeX, (int) policeY, POLICE_WIDTH, POLICE_HEIGHT);

        if (playerRect.intersects(policeRect)) {
            gameOver = true;
            timer.stop();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            this,
                            "Złapali Cię! KONIEC GRY",
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Antyaliasing / ładniejsze kształty
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tło
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        // Podłoga
        g2.setColor(new Color(60, 60, 80));
        g2.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);

        // Player
        g2.setColor(new Color(80, 180, 255)); // niebieski
        g2.fillRoundRect((int) playerX, (int) playerY, PLAYER_WIDTH, PLAYER_HEIGHT, 10, 10);

        // Police
        g2.setColor(new Color(255, 80, 80)); // czerwony
        g2.fillRoundRect((int) policeX, (int) policeY, POLICE_WIDTH, POLICE_HEIGHT, 10, 10);

        // Info na ekranie
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Sterowanie: A/D lub ←/→ = ruch, SPACE lub W = skok (double jump tylko Player)", 20, 25);
        g2.drawString("Uciekaj przed Policją! Gdy wasze postacie się dotkną – koniec gry.", 20, 45);

        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String txt = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(txt);
            g2.drawString(txt, (WIDTH - textWidth) / 2, HEIGHT / 2);
        }
    }

    // Sterowanie
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }

        // Skok / double jump
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (playerOnGround || playerJumpCount < 2) {
                playerVY = JUMP_STRENGTH;
                playerJumpCount++;
                playerOnGround = false;
            }
        }

        // Restart gry klawiszem R
        if (code == KeyEvent.VK_R && gameOver) {
            resetGame();
        }
    }

    private void resetGame() {
        playerX = 100;
        playerY = GROUND_Y - PLAYER_HEIGHT;
        playerVX = 0;
        playerVY = 0;
        playerJumpCount = 0;
        playerOnGround = true;

        policeX = 600;
        policeY = GROUND_Y - POLICE_HEIGHT;
        policeVX = 0;
        policeVY = 0;
        policeOnGround = true;

        gameOver = false;
        timer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // nie używamy
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Player vs Police - Chase Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            ChaseGame game = new ChaseGame();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            game.requestFocusInWindow();
        });
    }
}
