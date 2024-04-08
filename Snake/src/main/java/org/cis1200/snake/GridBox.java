package org.cis1200.snake;

import java.awt.*;

public class GridBox extends GameObj {

    public GridBox(int row, int column, int px, int py, int dim) {
        super(row, column, px, py, dim);

    }

    // draws gridbox
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(this.getPy(), this.getPx(), this.getDim(), this.getDim());
    }

    // used for overriding
    @Override
    public void move() {
        return;
    }


}