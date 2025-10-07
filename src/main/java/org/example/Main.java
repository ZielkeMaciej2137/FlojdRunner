package org.example;

import javax.swing.*;
import java.awt.*;



public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Środkowy palec");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.CYAN);
        centerPanel.setPreferredSize(new Dimension(300, 300));


        frame.add(centerPanel, new GridBagConstraints());

        frame.setVisible(true);
    }
}
 