package org.cis1200.snake;

import java.awt.*;
import java.util.ArrayList;

public class Bomb extends GridBox implements Edible {

    private ArrayList<GridBox> bombPositions;
    public Bomb(int row, int column, int px, int py, int dim, ArrayList<GridBox> bombPositions) {
        super(row, column, px, py, dim);

        this.bombPositions = bombPositions;
    }

    // getter methods
    @Override
    public ArrayList<GridBox> getPositions() {
        return this.bombPositions;
    }

    // setter methods
    @Override
    public void addEdible(GridBox b) {
        this.bombPositions.add(b);
    }

    // other methods

    @Override
    public void draw(Graphics g) {
        if (this == null || this.bombPositions == null) {
            return;
        }

        g.setColor(Color.BLACK);
        for (GridBox b : this.bombPositions) {
            g.fillRect(b.getPy(), b.getPx(), b.getDim(), b.getDim());
        }
    }

    @Override
    public void move() {
        return;
    }

    @Override
    public int eat(Snake s, Bomb bo, Board b, Boolean bombMode) {
        return s.getScore();
    }

}
