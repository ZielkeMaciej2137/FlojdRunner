package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkinButton extends JFrame {

    private final JButton changeSkinButton;
    private final JLabel infoLabel;

    // Tablica dostępnych "skórek" (kolorów)
    private final Color[] availableSkins = {
            Color.WHITE, // trudność 1
            Color.WHITE_PALE,      // trudność 2
            Color.MEXICAN,       // trudność 3
            Color.TANNED,     // trudność 4
            Color.BLACK         // trudność 5
    };


    private final int[] difficultyLevels = {1, 2, 3, 4, 5};

    private int currentIndex = 0; // aktualny kolor

    public SkinButton() {
        setTitle("Zmiana koloru skóry (poziomu trudności)");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        infoLabel = new JLabel("Aktualny kolor skóry: WHITE | Trudność: 1", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(infoLabel, BorderLayout.NORTH);

        changeSkinButton = new JButton("Zmień kolor skóry");
        changeSkinButton.setFont(new Font("Arial", Font.BOLD, 16));
        changeSkinButton.setBackground(java.awt.Color.DARK_GRAY);
        changeSkinButton.setForeground(java.awt.Color.WHITE);

        changeSkinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSkin();
            }
        });

        add(changeSkinButton, BorderLayout.CENTER);
        getContentPane().setBackground(availableSkins[currentIndex]);
    }

    // zmiana skóry i poziomu trudności
    private void changeSkin() {
        currentIndex = (currentIndex + 1) % availableSkins.length;

        Color newColor = availableSkins[currentIndex];
        int difficulty = difficultyLevels[currentIndex];

        getContentPane().setBackground(newColor);

        // Nazwa koloru (dla wyświetlenia)
        String colorName = getColorName(newColor);

        infoLabel.setText("Aktualny kolor skóry: " + colorName + " | Trudność: " + difficulty);
    }

    // pomocnicza metoda do uzyskania nazwy koloru
    private String getColorName(Color color) {
        if (color.equals(Color.WHITE)) return "WHITE";
        if (color.equals(Color.WHITE_PALE)) return "WHITE_PALE";
        if (color.equals(Color.MEXICAN)) return "MEXICAN";
        if (color.equals(Color.TANNED)) return "TANNED";
        if (color.equals(Color.BLACK)) return "BLACK";
        return "UNKNOWN(błąd)";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SkinButton().setVisible(true);
        });
    }
}
