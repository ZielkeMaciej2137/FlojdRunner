package org.example;

public class Player {
    private Kolor kolor;
    private double speed;
    private double jump;

    public Player(Kolor kolor, double speed, double jump) {
        this.kolor = kolor;
        this.speed = speed;
        this.jump = jump;
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



}

