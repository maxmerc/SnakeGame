package org.cis1200.snake;

import java.awt.*;
import java.util.ArrayList;

public class Food extends GridBox implements Edible {

    private ArrayList<GridBox> foodPositions;
    public Food(int row, int column, int px, int py, int dim, ArrayList<GridBox> foodPositions) {
        super(row, column, px, py, dim);

        this.foodPositions = foodPositions;
    }

    // getter methods
    @Override
    public ArrayList<GridBox> getPositions() {
        return this.foodPositions;
    }

    // setter methods
    @Override
    public void addEdible(GridBox b) {
        this.foodPositions.add(b);
    }

    // other methods

    @Override
    public void draw(Graphics g) {
        if (this == null || this.foodPositions == null) {
            return;
        }

        g.setColor(Color.RED);
        for (GridBox b : this.foodPositions) {
            g.fillRect(b.getPy(), b.getPx(), b.getDim(), b.getDim());
        }
    }

    @Override
    public void move() {
        return;
    }

    @Override
    public int eat(Snake s, Bomb bo, Board b, Boolean bombMode) {
        s.incrementScore();
        s.growSnake();
        b.addFood(s, this, bo, bombMode);
        return s.getScore();
    }

}
