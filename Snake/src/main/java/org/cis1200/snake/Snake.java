package org.cis1200.snake;

import java.awt.*;
import java.util.*;

public class Snake extends GameObj {

    private GridBox head;
    private LinkedList<GridBox> body;
    private int direction;
    private int score;
    private boolean moveAvailable;
    private LinkedList<GridBox> previousBoxes = new LinkedList<>();


    /** DIRECTION_KEY
     * 0 = Up
     * 1 = Right
     * 2 = Down
     * 3 = Left
     * Note: The snake shouldn't be able to do an immediate 180 in direction key
     */
    public static final HashMap<Integer, HashSet<Integer>> DIRECTION_KEY = new HashMap<>();

    public Snake(GridBox head, LinkedList<GridBox> body, int direction) {
        super(head.getRow(), head.getColumn(), head.getPx(), head.getPy(), head.getDim());
        this.head = head;
        this.body = body;
        this.direction = direction;
        this.score = 0;

        // Initializing the DIRECTION_KEY
        final HashSet<Integer> set0 = new HashSet<>();
        set0.add(1);
        set0.add(3);
        DIRECTION_KEY.put(0, set0);

        final HashSet<Integer> set1 = new HashSet<>();
        set1.add(0);
        set1.add(2);
        DIRECTION_KEY.put(1, set1);

        final HashSet<Integer> set2 = new HashSet<>();
        set2.add(1);
        set2.add(3);
        DIRECTION_KEY.put(2, set2);

        final HashSet<Integer> set3 = new HashSet<>();
        set3.add(0);
        set3.add(2);
        DIRECTION_KEY.put(3, set3);

    }

    // getter methods
    public int getScore() {
        return this.score;
    }

    public LinkedList<GridBox> getBody() {
        return this.body;
    }

    public boolean hasMoveAvailable() {
        return this.moveAvailable;
    }

    public int getLength() {
        if (body == null) {
            return -1;
        }
        return this.body.size();
    }

    public int getDirection() {
        return this.direction;
    }

    public GridBox getHead() {
        return this.head;
    }

    @Override
    public int getRow() {
        if (head != null) {
            return this.head.getRow();
        }
        return -1;
    }

    @Override
    public int getColumn() {
        if (head != null) {
            return this.head.getColumn();
        }
        return -1;
    }

    // setter methods
    public void setHead(GridBox b) {
        this.head = b;
        this.body.addFirst(b);
    }

    public void incrementScore() {
        this.score++;
    }

    public void changeDirection(int dir) {
        if (DIRECTION_KEY.get(this.direction).contains(dir)) {
            this.direction = dir;
        }
    }

    public void setMoveAvailable(boolean b) {
        this.moveAvailable = b;
    }

    // other methods

    public void addBodyPart(GridBox b) {
        this.body.add(b);
    }

    @Override
    public void draw(Graphics g) {
        if (this == null) {
            return;
        }

        g.setColor(SnakeGameCourt.BODY_COLOR);
        for (GridBox b : this.body) {
            if (head == b) {
                g.setColor(SnakeGameCourt.HEAD_COLOR);
                g.fillRect(b.getPy(), b.getPx(),
                        b.getDim(), b.getDim());
                g.setColor(SnakeGameCourt.BODY_COLOR);
            } else {
                g.fillRect(b.getPy(), b.getPx(),
                        b.getDim(), b.getDim());
            }
        }
    }

    @Override
    public void move() {
        GridBox tail = this.body.removeLast();
        this.previousBoxes.addFirst(tail);

        // We only want to store the previous 3 Tiles.
        while (this.previousBoxes.size() > 3) {
            this.previousBoxes.removeLast();
        }
    }

    public boolean nextMoveAvailable(Board b) {
        if (this == null || this.body == null) {
            System.out.println("Snake is null");
        }
        if (b == null) {
            System.out.println("Board is null");
        }

        int newHeadRow = this.getRow();
        int newHeadColumn = this.getColumn();
        GridBox h;
        if (this.direction ==  0) {
            newHeadRow--;
        } else if (this.direction == 1) {
            newHeadColumn++;
        } else if (this.direction == 2) {
            newHeadRow++;
        } else if (this.direction == 3) {
            newHeadColumn--;
        } else {
            System.out.println("Direction is invalid: " + this.direction);
        }

        // if snake outside grid --> game over
        if (newHeadRow < 0 ||
                newHeadColumn < 0 ||
                newHeadRow >= b.getRow() ||
                newHeadColumn >= b.getColumn()) {
            return false;
        }

        h = b.getBox(newHeadRow, newHeadColumn);

        if (h == null) {
            return false;
        }

        this.setHead(h);
        return true;
    }

    public boolean intersects(Collection<GridBox> grid, int start) {
        if (grid == null) {
            System.out.println("no gridBoxes");
            return false;
        }

        int index = 0;
        for (GridBox b : grid) {
            if (this.head == b && start <= index) {
                grid.remove(b);
                return true;
            }
            index++;
        }

        return false;
    }

    public void growSnake() {
        GridBox newBody;
        if (this.previousBoxes.size() >= 1) {
            newBody = this.previousBoxes.removeFirst();
            this.addBodyPart(newBody);
        }
    }

    // used to reset the snake
    @Override
    public void reset() {
        this.head.reset();
        this.body = null;
        this.direction = -1;
        this.score = 0;
        this.moveAvailable = false;
        this.previousBoxes = new LinkedList<>();
    }

}
