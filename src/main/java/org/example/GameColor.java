package org.example;

import java.awt.*;


public enum GameColor {

    EASY_COLOR(new Color(76, 175, 80)),     // zielony
    NORMAL_COLOR(new Color(246, 232, 108)),  // zolty
    HARD_COLOR(new Color(255, 152, 0)),     // pomarańczowy
    INSANE_COLOR(new Color(244, 67, 54));   // czerwony

    private final Color awtColor;

    GameColor(Color awtColor) {
        this.awtColor = awtColor;
    }

    public Color getAwtColor() {
        return awtColor;
    }
}
