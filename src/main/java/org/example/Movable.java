package org.example;

public class Movable {
    private double speed, posX, posY;

    Movable(double speed, double jump) {
        this.speed = speed;
    }

    void moveLeft() {posX += speed;}

    void moveRight() {posX -= speed;}

    public double getSpeed() {return speed;}

    public void setSpeed(double speed) {this.speed = speed;}

    public double getPosX() {return posX;}

    public void setPosX(int posX) {this.posX = posX;}

    public double getPosY() {return posY;}

    public void setPosY(int posY) {this.posY = posY;}


}
