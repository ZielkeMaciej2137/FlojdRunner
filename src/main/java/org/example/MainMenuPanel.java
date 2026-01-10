package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Panel menu głównego gry
 */
public class MainMenuPanel extends JPanel {

    private Difficulty selectedDifficulty = Difficulty.NORMAL;
    private JPanel previewPanel;

    public MainMenuPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20));

        add(createTopBar(), BorderLayout.NORTH);
        add(createCenter(), BorderLayout.CENTER);
    }

    // ===== GÓRNY PASEK (TYTUŁ + WYJŚCIE) =====
    private JComponent createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(30, 30, 30));
        top.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("FLOJD RUNNER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        ExitButton exitButton = new ExitButton();



        top.add(title, BorderLayout.WEST);
        top.add(exitButton, BorderLayout.EAST);

        return top;
    }

    // ===== ŚRODEK MENU =====
    private JComponent createCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setOpaque(false);

        center.add(createLeftPanel());
        center.add(createRightPanel());

        return center;
    }

    // ===== LEWA STRONA (TRUDNOŚĆ) =====
    private JComponent createLeftPanel() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(80, 120, 80, 40));

        JLabel label = new JLabel("Poziom trudności");
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(label);
        left.add(Box.createVerticalStrut(30));

        for (Difficulty difficulty : Difficulty.values()) {
            DifficultyButton button = new DifficultyButton(difficulty);
            button.addActionListener(e -> selectDifficulty(difficulty));
            left.add(button);
            left.add(Box.createVerticalStrut(15));
        }

        return left;
    }

    // ===== PRAWA STRONA (PODGLĄD + GRAJ) =====
    private JComponent createRightPanel() {
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 120));

        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(300, 300));
        previewPanel.setMaximumSize(new Dimension(300, 300));
        previewPanel.setBackground(GameColor.NORMAL_COLOR.getAwtColor());

        JButton playButton = createPlayButton();

        right.add(previewPanel);
        right.add(Box.createVerticalStrut(40));
        right.add(playButton);

        return right;
    }

    // ===== WYBÓR TRUDNOŚCI =====
    private void selectDifficulty(Difficulty difficulty) {
        selectedDifficulty = difficulty;

        previewPanel.setBackground(
                switch (difficulty) {
                    case EASY -> GameColor.EASY_COLOR.getAwtColor();
                    case NORMAL -> GameColor.NORMAL_COLOR.getAwtColor();
                    case HARD -> GameColor.HARD_COLOR.getAwtColor();
                    case INSANE -> GameColor.INSANE_COLOR.getAwtColor();
                }
        );
    }

    // ===== PRZYCISK GRAJ =====
    private JButton createPlayButton() {
        JButton play = new JButton("GRAJ");
        play.setFont(new Font("Segoe UI", Font.BOLD, 22));
        play.setBackground(new Color(76, 175, 80));
        play.setForeground(Color.WHITE);
        play.setFocusPainted(false);
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.setPreferredSize(new Dimension(200, 60));
        play.setMaximumSize(new Dimension(200, 60));

        play.addActionListener(e -> startGame());

        return play;
    }

    // ===== START GRY (DZIAŁA) =====
    private void startGame() {
        System.out.println("Start gry – trudność: " + selectedDifficulty);
        // tutaj później: przełączenie na panel gry
    }



}
