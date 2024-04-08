package org.cis1200.snake;
import java.awt.*;
import java.util.ArrayList;

public interface Edible {
    public ArrayList<GridBox> getPositions();
    public void addEdible(GridBox b);
    public int eat(Snake s, Bomb bo, Board b, Boolean bombMode);
    public void draw(Graphics g);
    public void reset();

}
