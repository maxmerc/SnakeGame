package org.cis1200.snake;

import java.awt.*;

/**
 * This class will store abstract functionality for the following:
 * (1) snake
 * (2) gridbox --> which is then used for food and bomb
 */
public abstract class GameObj {

    // row and col of gameObj on board
    private int row;
    private int column;

    // objects dim in pixels
    private int dim;

    // location in pixels
    private int px;
    private int py;

    public GameObj(int row, int column, int px, int py, int dim) {
        this.row = row;
        this.column = column;
        this.dim = dim;
        this.px = px;
        this.py = py;
    }

    // getter methods

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int getDim() {
        return this.dim;
    }

    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    // setter methods

    public void setRow(int newRow) {
        this.row = newRow;
    }

    public void setColumn(int newColumn) {
        this.column = newColumn;
    }

    // abstract methods

    public abstract void draw(Graphics g);

    public abstract void move();

    public  void reset() {
        this.row = -1;
        this.column = -1;
        this.dim = 0;
        this.px = -1;
        this.py = -1;
    }
}
