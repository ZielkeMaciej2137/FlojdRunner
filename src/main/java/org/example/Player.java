package org.example;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private SkinColor kolor;
    private double speed;
    private double jump;
    private int kondycja;
    public static final Map<SkinColor, Color> koloryTablica = new HashMap<SkinColor, Color>();

    static {
    koloryTablica.put(SkinColor.WHITE, Color.WHITE);
    koloryTablica.put(SkinColor.WHITE_PALE, new java.awt.Color(232, 232, 232));
    koloryTablica.put(SkinColor.MEXICAN, new java.awt.Color(205, 133, 63));
    koloryTablica.put(SkinColor.TANNED, new java.awt.Color(210, 180, 140));
    koloryTablica.put(SkinColor.BLACK, Color.BLACK);
    }

    public Player(SkinColor kolor, double speed, double jump, int kondycja) {
        this.kolor = kolor;
        this.speed = speed;
        this.jump = jump;
        this.kondycja = kondycja;
    }

    public SkinColor getKolor() {
        return kolor;
    }
    public void setKolor(SkinColor kolor) {
        this.kolor = kolor;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public double getJump() {
        return jump;
    }
    public void setJump(double jump) {
        this.jump = jump;
    }
    public int getKondycja() {
        return kondycja;
    }
    public void setKondycja(int kondycja) {
        this.kondycja = kondycja;
    }




}

