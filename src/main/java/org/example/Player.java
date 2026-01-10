package org.example;

import java.awt.*;


public class Player {

    private Difficulty difficulty;
    private double speed;
    private double jump;
    private int stamina;

    public Player(Difficulty difficulty) {
        this.difficulty = difficulty;
        applyDifficultyStats();
    }

    /**
     * Ustawia statystyki na podstawie trudności
     */
    private void applyDifficultyStats() {
        switch (difficulty) {
            case EASY -> {
                speed = 4.0;
                jump = 8.0;
                stamina = 150;
            }
            case NORMAL -> {
                speed = 5.0;
                jump = 7.0;
                stamina = 120;
            }
            case HARD -> {
                speed = 6.0;
                jump = 6.0;
                stamina = 90;
            }
            case INSANE -> {
                speed = 7.5;
                jump = 5.0;
                stamina = 60;
            }
        }
    }

    /**
     * Zwraca kolor gracza zależny od trudności
     */
    public Color getPlayerColor() {
        return switch (difficulty) {
            case EASY -> GameColor.EASY_COLOR.getAwtColor();
            case NORMAL -> GameColor.NORMAL_COLOR.getAwtColor();
            case HARD -> GameColor.HARD_COLOR.getAwtColor();
            case INSANE -> GameColor.INSANE_COLOR.getAwtColor();
        };
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
