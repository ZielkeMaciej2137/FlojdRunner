package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButton extends JFrame {

    public ExitButton() {
        setTitle("Aplikacja z przyciskiem wyjścia");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton exitButton = new JButton("Wyjście");

        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBackground(java.awt.Color.RED);
        exitButton.setForeground(java.awt.Color.WHITE);


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(exitButton, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExitButton().setVisible(true);
        });
    }
}
