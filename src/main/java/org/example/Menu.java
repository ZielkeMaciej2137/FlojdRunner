package org.example;

import javax.swing.*;
import java.awt.*;

public class Menu {
    public static void main(String[] args) {
        // główne okno
        JFrame frame = new JFrame("FlojdRunner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainpanel = new JPanel(new BorderLayout());
        SkinButton skinButton = new SkinButton();
        ExitButton exitButton = new ExitButton();


        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(skinButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(exitButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        mainpanel.add(topPanel, BorderLayout.NORTH);

        frame.add(mainpanel);



        // widoczne okno
        frame.setVisible(true);
    }
}
