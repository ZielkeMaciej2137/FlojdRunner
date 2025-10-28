package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class Frame extends javax.swing.JFrame {
    public Frame() {
        JFrame frame = new JFrame("Flojd Runner");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.RED);
        centerPanel.setPreferredSize(new Dimension(300, 300));


        frame.add(centerPanel, new GridBagConstraints());

        frame.setVisible(true);
    }
}
