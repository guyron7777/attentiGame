package com.GuyRon.attentigame.models;

public class ColorsObj {
    private int row;
    private int col;
    private int colorNumber;

    public ColorsObj(int row, int col, int colorNumber) {
        this.row = row;
        this.col = col;
        this.colorNumber = colorNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }
}
