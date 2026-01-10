package org.example;

import javax.swing.*;

/**
 * Główne okno gry – uruchamiane w trybie pełnoekranowym
 */
public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Flojd Runner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // brak ramki systemowej
        setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen

        setContentPane(new MainMenuPanel());

        setVisible(true);
    }
}
