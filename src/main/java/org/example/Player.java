package org.example;

public class Player {
    private Kolor kolor;
    private double speed;
    private double jump;
    private int kondycja;

    public Player(Kolor kolor, double speed, double jump, int kondycja) {
        this.kolor = kolor;
        this.speed = speed;
        this.jump = jump;
        this.kondycja = kondycja;
    }

    public Kolor getKolor() {
        return kolor;
    }
    public void setKolor(Kolor kolor) {
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

