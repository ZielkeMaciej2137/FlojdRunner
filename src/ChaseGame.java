import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChaseGame extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 450;

    private static final int GROUND_Y = 360;

    private static final int PLAYER_WIDTH = 40;
    private static final int PLAYER_HEIGHT = 60;
    private static final int POLICE_WIDTH = 40;
    private static final int POLICE_HEIGHT = 60;

    private static final int PLAYER_SPEED = 6;
    private static final int POLICE_SPEED = 4;

    private static final double GRAVITY = 0.6;
    private static final double JUMP_STRENGTH = -12.0;

    private double playerX = 100;
    private double playerY = GROUND_Y - PLAYER_HEIGHT;
    private double playerVX = 0;
    private double playerVY = 0;

    private double policeX = 600;
    private double policeY = GROUND_Y - POLICE_HEIGHT;
    private double policeVX = 0;
    private double policeVY = 0;

    private int playerJumpCount = 0; // 0, 1, 2
    private boolean playerOnGround = true;
    private boolean policeOnGround = true;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private Timer timer;
    private boolean gameOver = false;

    ArrayList<JComponent> walls;

    public ChaseGame() {
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(30, 30, 40));

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        Wall wall = new Wall(Color.GREEN);
        wall.setBounds(WIDTH/2,GROUND_Y-300,20,300);
        add(wall);

        walls = new ArrayList();
        walls.add(wall);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updatePlayer();
            wallCollision();

            //updatePolice();
            //checkCollision();
        }
        repaint();
    }

    private void updatePlayer() {
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

        playerVY += GRAVITY;
        playerY += playerVY;

        if (playerY >= GROUND_Y - PLAYER_HEIGHT) {
            playerY = GROUND_Y - PLAYER_HEIGHT;
            playerVY = 0;
            if (!playerOnGround) {
                playerOnGround = true;
                playerJumpCount = 0;
            }
        } else {
            playerOnGround = false;
        }
    }

    private void updatePolice() {
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

        if (playerY + PLAYER_HEIGHT < policeY + POLICE_HEIGHT - 10 && policeOnGround) {
            policeVY = JUMP_STRENGTH;
            policeOnGround = false;
        }

        policeVY += GRAVITY;
        policeY += policeVY;

        if (policeY >= GROUND_Y - POLICE_HEIGHT) {
            policeY = GROUND_Y - POLICE_HEIGHT;
            policeVY = 0;
            policeOnGround = true;
        }
    }

    private void wallCollision() {
        Rectangle playerRect = new Rectangle((int) playerX, GROUND_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        for(JComponent wall : walls) {
            Rectangle wallRect = wall.getBounds();
            if(playerRect.intersects(wallRect)) {
                SwingUtilities.invokeLater(()->{
                    JOptionPane.showMessageDialog(this,"WALL COLLISION","!",JOptionPane.INFORMATION_MESSAGE);
                });
            }
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

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(new Color(60, 60, 80));
        g2.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);

        g2.setColor(new Color(80, 180, 255));
        g2.fillRoundRect((int) playerX, (int) playerY, PLAYER_WIDTH, PLAYER_HEIGHT, 10, 10);

        g2.setColor(new Color(255, 80, 80));
        g2.fillRoundRect((int) policeX, (int) policeY, POLICE_WIDTH, POLICE_HEIGHT, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Sterowanie: A/D lub ←/→ = ruch, SPACE lub W = skok (double jump tylko Player)", 20, 25);
        g2.drawString("Uciekaj przed Policją! Gdy wasze postacie się dotkną – koniec gry.", 20, 45);

        //Walls

        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String txt = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(txt);
            g2.drawString(txt, (WIDTH - textWidth) / 2, HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (playerOnGround || playerJumpCount < 2) {
                playerVY = JUMP_STRENGTH;
                playerJumpCount++;
                playerOnGround = false;
            }
        }
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
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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
