package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkinButton extends JButton {

    private final JLabel infoLabel;

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground()); // zawsze jest tło przycisku dzieki czemu nie ma niebieskiego zaaznaczania przy kliknieciu
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    // Tablica dostępnych "skórek" (kolorów)
    private final SkinColor[] availableSkins = {
            SkinColor.WHITE,         // trudność 1
            SkinColor.WHITE_PALE,    // trudność 2
            SkinColor.MEXICAN,       // trudność 3
            SkinColor.TANNED,        // trudność 4
            SkinColor.BLACK          // trudność 5
    };

    private final int[] difficultyLevels = {1, 2, 3, 4, 5};

    private int currentIndex = 0; // aktualny kolor

    public SkinButton() {
        super("Zmień kolor skóry");
        setFont(new Font("Arial", Font.BOLD, 16));
        setPreferredSize(new Dimension(250, 120));
        setLayout(new BorderLayout());
        setBackground(Player.koloryTablica.get(availableSkins[currentIndex])); // ustaw kolor
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(true);
        setContentAreaFilled(false);



        infoLabel = new JLabel("Aktualny kolor skóry: WHITE | Trudność: 1", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(infoLabel, BorderLayout.NORTH);


        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSkin();
            }
        });

        // kolor tła zależny od aktualnej skórki
        setBackground(Player.koloryTablica.get(availableSkins[currentIndex]));
    }

    // zmiana skóry i poziomu trudności
    private void changeSkin() {
        currentIndex = (currentIndex + 1) % availableSkins.length;

        SkinColor newSkinColor = availableSkins[currentIndex];
        int difficulty = difficultyLevels[currentIndex];

        setBackground(Player.koloryTablica.get(newSkinColor));

        // Nazwa koloru (dla wyświetlenia)
        String colorName = getColorName(newSkinColor);

        infoLabel.setText("Aktualny kolor skóry: " + colorName + " | Trudność: " + difficulty);
    }

    // pomocnicza metoda do uzyskania nazwy koloru
    private String getColorName(SkinColor skinColor) {
        if (skinColor.equals(SkinColor.WHITE)) return "WHITE";
        if (skinColor.equals(SkinColor.WHITE_PALE)) return "WHITE_PALE";
        if (skinColor.equals(SkinColor.MEXICAN)) return "MEXICAN";
        if (skinColor.equals(SkinColor.TANNED)) return "TANNED";
        if (skinColor.equals(SkinColor.BLACK)) return "BLACK";
        return "UNKNOWN(błąd)";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SkinButton button = new SkinButton();

            JFrame frame = new JFrame("Skin Button");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 250);
            frame.setLayout(new BorderLayout());
            frame.add(button, BorderLayout.CENTER);
            frame.setVisible(true);



        });
    }
}
