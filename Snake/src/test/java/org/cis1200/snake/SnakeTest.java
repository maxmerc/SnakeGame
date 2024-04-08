package org.cis1200.snake;

import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


/*
 * test file
 */

public class SnakeTest {

    // snake pos and direction tests
    @Test
    public void testSnakeDirectionUp() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(6, s.getRow());
        assertEquals(4, s.getColumn());

        assertEquals(0, s.getDirection());

        b.reset();
        s.reset();
    }

    @Test
    public void snakeDirDownTest() {
        String filePath = "files/TEST_2.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(3, s.getRow());
        assertEquals(7, s.getColumn());

        assertEquals(2, s.getDirection());

        b.reset();
        s.reset();
    }

    @Test
    public void snakeDirRightTest() {
        String filePath = "files/TEST_3.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(8, s.getRow());
        assertEquals(3, s.getColumn());

        assertEquals(1, s.getDirection());

        b.reset();
        s.reset();
    }

    @Test
    public void snakeDirLeftTest() {
        String filePath = "files/TEST_4.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(7, s.getRow());
        assertEquals(5, s.getColumn());

        assertEquals(3, s.getDirection());

        b.reset();
        s.reset();
    }

    // snake moves correctly

    @Test
    public void snakeMovementTest() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(6, s.getRow());
        assertEquals(4, s.getColumn());

        for (int i = 0; i < 3; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(6 - i - 1, s.getRow());
            assertEquals(4, s.getColumn());
        }

        b.reset();
        s.reset();
    }

    @Test
    public void snakeTurnTest() {
        String filePath = "files/TEST_3.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(8, s.getRow());
        assertEquals(3, s.getColumn());

        for (int i = 0; i < 2; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(8, s.getRow());
            assertEquals(3 + i + 1, s.getColumn());
        }

        s.changeDirection(0);
        for (int i = 0; i < 4; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(8 - i - 1, s.getRow());
            assertEquals(5, s.getColumn());
        }

        b.reset();
        s.reset();
    }

    @Test
    public void snakeEatsFoodTest() {
        String filePath = "files/TEST_2.txt";

        Board b;
        Snake s;
        Food f;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();
        f = b.setFood();

        assertEquals(3, s.getRow());
        assertEquals(7, s.getColumn());

        s.changeDirection(3);

        // The Snake hits the food on the fifth iteration.
        for (int i = 0; i < 5; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(3, s.getRow());
            assertEquals(7 - i - 1, s.getColumn());
        }

        assertTrue(s.intersects(f.getPositions(), 0));

        b.reset();
        s.reset();
        f.reset();
    }

    @Test
    public void snakeEatsBombTest() {
        String filePath = "files/TEST_5.txt";

        Board b;
        Snake s;
        Food f;
        Bomb bo;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();
        f = b.setFood();
        bo = b.setBomb();

        assertEquals(6, s.getRow());
        assertEquals(4, s.getColumn());


        // The Snake hits bomb on second iteration
        for (int i = 0; i < 2; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(6 - i - 1, s.getRow());
            assertEquals(4, s.getColumn());
        }

        assertTrue(s.intersects(bo.getPositions(), 0));

        b.reset();
        s.reset();
        f.reset();
        bo.reset();
    }

    @Test
    public void snakeIntersectWallTest() {
        String filePath = "files/TEST_2.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(3, s.getRow());
        assertEquals(7, s.getColumn());

        s.changeDirection(1);

        // The Snake hits the food on the fifth iteration.
        for (int i = 0; i < 2; i++) {
            if (s.nextMoveAvailable(b)) {
                s.move();
            }

            assertEquals(3, s.getRow());
            assertEquals(7 + i + 1, s.getColumn());
        }

        assertFalse(s.nextMoveAvailable(b));

        b.reset();
        s.reset();
    }

    // testing reset

    @Test
    public void resetSnakeTest() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        s.reset();
        assertEquals(-1, s.getLength());
        assertEquals(-1, s.getDirection());
        assertEquals(0, s.getScore());
        assertFalse(s.hasMoveAvailable());

        b.reset();
    }

    @Test
    public void resetBoardTest() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        b.reset();
        assertNull(b.getGameBoard());

        s.reset();
    }

    @Test
    public void resetFoodTest() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;
        Food f;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();
        f = b.setFood();

        f.reset();

        assertEquals(-1, f.getRow());
        assertEquals(-1, f.getColumn());
        assertEquals(0, f.getDim());
        assertEquals(-1, f.getPx());
        assertEquals(-1, f.getPy());

        s.reset();
        b.reset();
    }

    @Test
    public void resetBombTest() {
        String filePath = "files/TEST_5.txt";

        Board b;
        Snake s;
        Bomb bo;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();
        bo = b.setBomb();

        bo.reset();

        assertEquals(-1, bo.getRow());
        assertEquals(-1, bo.getColumn());
        assertEquals(0, bo.getDim());
        assertEquals(-1, bo.getPx());
        assertEquals(-1, bo.getPy());

        s.reset();
        b.reset();
    }

    // misc tests

    @Test
    public void badBoardTest() {
        String filePath = "files/INVALID_BOARD.txt";

        File boardFile = new File(filePath);

        assertThrows(IllegalArgumentException.class, () -> {
            new Board(boardFile);
        });
    }

    @Test
    public void incScoreTest() {
        String filePath = "files/TEST_1.txt";

        Board b;
        Snake s;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();

        assertEquals(0, s.getScore());
        s.incrementScore();
        assertEquals(1, s.getScore());
        s.incrementScore();
        s.incrementScore();
        s.incrementScore();
        s.incrementScore();
        assertEquals(5, s.getScore());

        b.reset();
        s.reset();
    }

    @Test
    public void difNumFoodTest() {
        String filePath = "files/TEST_3.txt";

        Board b;
        Food f;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        f = b.setFood();

        assertEquals(5, f.getPositions().size());

        f.reset();
        b.reset();
    }

    // it is useful to clear the text file before using it and after
    @Test
    public void saveFileTest() throws IOException {
        String filePath = "files/TEST_5.txt";

        Board b;
        Snake s;
        Food f;
        Bomb bo;

        File boardFile = new File(filePath);
        b = new Board(boardFile);
        b.setGrid();
        s = b.setSnake();
        f = b.setFood();
        bo = b.setBomb();

        s.move();
        int headRow = s.getRow();
        int headCol = s.getColumn();
        GridBox food = f.getPositions().get(0);
        int foodRow = food.getRow();
        int foodCol = food.getColumn();
        GridBox bomb = bo.getPositions().get(0);
        int bombRow = bomb.getRow();
        int bombCol = bomb.getColumn();

        File saveFile = new File("files/SAVE_TEST.txt");
        BufferedWriter br = new BufferedWriter(new FileWriter(saveFile));
        br.write("");
        br.flush();
        b.saveBoardFile(saveFile, s, f, bo, b.getBombMode());

        Board newB = new Board(saveFile);
        newB.setGrid();
        Snake newS = newB.setSnake();
        Food newF = newB.setFood();
        Bomb newBo = newB.setBomb();
        int newHeadRow = newS.getRow();
        int newHeadCol = newS.getColumn();
        GridBox food1 = newF.getPositions().get(0);
        int newFoodRow = food1.getRow();
        int newFoodCol = food1.getColumn();
        GridBox bomb1 = newBo.getPositions().get(0);
        int newBombRow = bomb1.getRow();
        int newBombCol = bomb1.getColumn();

        assertEquals(headRow, newHeadRow);
        assertEquals(headCol, newHeadCol);
        assertEquals(foodRow, newFoodRow);
        assertEquals(foodCol, newFoodCol);
        assertEquals(bombRow, newBombRow);
        assertEquals(bombCol, newBombCol);

        br.write("");
        br.flush();
        br.close();

    }

}