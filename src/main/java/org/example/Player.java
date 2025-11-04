package org.example;

public class Player extends Movable {
    private SkinColor skinColor;

    public Player(SkinColor skinColor, double speed, double jump) {
        super(speed, jump);
        this.skinColor = skinColor;
    }

    public SkinColor getSkinColor() {return skinColor;}

    public void setSkinColor(SkinColor skinColor) {this.skinColor = skinColor;}

}

