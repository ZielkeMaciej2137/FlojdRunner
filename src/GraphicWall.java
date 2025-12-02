import javax.swing.*;
import java.awt.*;

public class GraphicWall extends JComponent {
    public GraphicWall(int posX, int posY, int width, int height, Color color) {
        logicWall = new LogicWall(posX, posY, width, height);
        this.color = color;

        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.fillRect(logicWall.posX, logicWall.posY, logicWall.width, logicWall.height);
    }
    public Color color;
    private final LogicWall logicWall;
}