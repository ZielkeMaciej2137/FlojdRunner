package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Przycisk wyboru trudności
 */
public class DifficultyButton extends JButton {

    public DifficultyButton(Difficulty difficulty) {
        setText(difficulty.name());
        setFont(new Font("Segoe UI", Font.BOLD, 18));
        setForeground(Color.WHITE);
        setBackground(getColor(difficulty));
        setFocusPainted(false);
        setBorderPainted(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(220, 50));
        setMaximumSize(new Dimension(220, 50));
    }

    private Color getColor(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> GameColor.EASY_COLOR.getAwtColor();
            case NORMAL -> GameColor.NORMAL_COLOR.getAwtColor();
            case HARD -> GameColor.HARD_COLOR.getAwtColor();
            case INSANE -> GameColor.INSANE_COLOR.getAwtColor();
        };
    }
}
