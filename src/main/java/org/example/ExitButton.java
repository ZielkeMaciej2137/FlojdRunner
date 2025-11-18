package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class ExitButton extends JButton {



    public ExitButton() {
        super("<html><img src='file:C:\\Users\\uczen\\Desktop\\FlojdRunner-adam\\FlojdRunner-adam\\src\\main\\java\\org\\example\\logoutIcon.png'></html>");
        setPreferredSize(new Dimension(50, 50));


        setFont(new Font("Arial", Font.BOLD, 16));
        setBackground(java.awt.Color.RED);
        setForeground(java.awt.Color.WHITE);




        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExitButton().setVisible(true);
        });
    }
}
