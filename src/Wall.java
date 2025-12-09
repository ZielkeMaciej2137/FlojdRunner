import javax.swing.*;
import java.awt.*;

public class Wall extends JComponent {
    public Wall(Color color) {
        this.color = color;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.fillRect(0,0, getWidth(), getHeight());
    }
    public Color color;
}