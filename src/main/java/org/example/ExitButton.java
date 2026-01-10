package org.example;

import javax.swing.*;
import java.awt.*;


/**
 * Uniwersalny przycisk wyjścia z gry
 * Może być używany w menu, pauzie, ekranie śmierci itp.
 */
public class ExitButton extends JButton {

    private final Color normalColor = new Color(180, 50, 50);


    public ExitButton() {

        // ===== IKONA =====
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/logoutIcon.png")
        );

        Image scaled = icon.getImage()
                .getScaledInstance(24, 24, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaled));

        // ===== WYGLĄD =====
        setBackground(normalColor);
        setOpaque(true);                 // rysuj tło
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setPreferredSize(new Dimension(48, 48));
        setCursor(new Cursor(Cursor.HAND_CURSOR));


        // ===== DZIAŁANIE =====
        addActionListener(e -> System.exit(0));
    }
}
