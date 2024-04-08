package org.cis1200.snake;
import java.io.*;

import java.awt.*;
import java.util.*;

public class Board extends GameObj {

    private GridBox[][] gameBoard = null;
    private ArrayList<int[]> snakeBoxes = new ArrayList<>();
    private ArrayList<int[]> foodBoxes = new ArrayList<>();
    private ArrayList<int[]> bombBoxes = new ArrayList<>();
    private int topLeftX;
    private int topLeftY;
    private boolean bombMode;
    private int[][] fileBoard;


    public Board(File boardFile) {
        // Board row, column, px, and py are initialized to INVALID VALUES
        super(-1, -1, -1, -1, SnakeGameCourt.BOARD_DIMENSION);
        bombMode = false;
        try {
            this.readBoardFile(boardFile);
        } catch (IOException exception) {
            System.out.println("Internal Error: " + exception.getMessage());
            System.out.println("Error must be fixed");
        }
    }

    // getter methods
    public GridBox getBox(int row, int column) {
        if (!(row >= 0 && column >= 0 &&
                row <= this.getRow() &&
                column <= this.getColumn())) {
            System.out.println("getBox call is out of grid");
        }
        return this.gameBoard[row][column];
    }

    public boolean getBombMode() {
        return this.bombMode;
    }

    public GridBox[][] getGameBoard() {
        return this.gameBoard;
    }

    public ArrayList<int[]> getSnakeTiles() {
        return this.snakeBoxes;
    }

    public ArrayList<int[]> getFoodTiles() {
        return this.foodBoxes;
    }

    public ArrayList<int[]> getBombBoxes() {
        return bombBoxes;
    }
    // other methods

    @Override
    public void draw(Graphics g) {
        if (this == null || this.gameBoard == null) {
            return;
        }

        for (GridBox[] boxRow : this.gameBoard) {
            for (GridBox b : boxRow) {
                b.draw(g);
            }
        }
    }

    @Override
    public void move() {
        return;
    }

    /* This method is the main part using file I/O
     * It first reads in the game board
     * Next it locates the snake and indicating its direction
     * Finally, it finds where the grid should be placed on the JFrame
     */
    public void readBoardFile(File file) throws IOException,
            IllegalArgumentException {
        try {
            // reading in game board
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<ArrayList<String>> tempBoard  = new ArrayList<>();

            String line;
            int columns = -1;
            int rows = 0;
            ArrayList<String> currRow;

            while (true) {
                line = br.readLine();

                if (line == null) {
                    break;
                }

                rows++;

                // if the rows are not all the same length
                if (line.length() != columns && columns != -1) {
                    throw new IllegalArgumentException("Invalid gameBoard");
                }

                columns = line.length();

                currRow = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    currRow.add(String.valueOf(line.charAt(i)));
                }
                tempBoard.add(currRow);
            }

            this.gameBoard = new GridBox[rows][columns];
            this.fileBoard = new int[rows][columns];
            this.setRow(rows);
            this.setColumn(columns);

            /* locating food, bombs, and snake direction
             * 0 = Open
             * 1 = Snake body
             * 2 = Snake head
             * 3 = Food
             * 4 = Bomb
             * Then depth first search for snake pos
             * */
            int headRow = -1;
            int headCol = -1;
            int currPosition;
            int[] foodPosition;
            int[] bombPosition;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    currPosition = Integer.parseInt(tempBoard.get(i).get(j));
                    this.fileBoard[i][j] = currPosition;

                    if (currPosition == 2) {
                        headRow = i;
                        headCol = j;
                    } else if (currPosition == 3) {
                        foodPosition = new int[] {i, j};
                        this.foodBoxes.add(foodPosition);
                    } else if (currPosition == 4) {
                        bombPosition = new int[] {i, j};
                        this.bombBoxes.add(bombPosition);
                        bombMode = true;
                    }

                    if ((currPosition < 0) || (currPosition > 4)) {
                        throw new IllegalArgumentException("Invalid Board value");
                    }
                }
            }
            if (headRow == -1 && headCol == -1) {
                throw new IllegalArgumentException("No snake included in file");
            }
            if (this.foodBoxes.size() == 0) {
                throw new IllegalArgumentException("No food included in file");
            }
            int[] snakeHead = {headRow, headCol};
            this.snakeBoxes.add(snakeHead);
            snakeDepthFirst(headRow + 1, headCol, this.fileBoard, this.snakeBoxes);
            snakeDepthFirst(headRow - 1, headCol, this.fileBoard, this.snakeBoxes);
            snakeDepthFirst(headRow, headCol + 1, this.fileBoard, this.snakeBoxes);
            snakeDepthFirst(headRow, headCol - 1, this.fileBoard, this.snakeBoxes);

        } catch (FileNotFoundException exception) {
            throw new FileNotFoundException("The given file could not be found");
        } catch (IOException exception) {
            throw new IOException("IOException encountered while reading file");
        }
    }

    // the writing with file I/O
    public void saveBoardFile(File file, Snake s, Food f, Bomb b, Boolean bombMode)
            throws IOException, IllegalArgumentException {
        try {
            ArrayList<GridBox> foodPos = f.getPositions();
            ArrayList<GridBox> bombPos = null;
            if (bombMode) {
                bombPos = b.getPositions();
            }
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write("");
            br.flush();
            for (int row = 0; row < gameBoard.length; row++) {
                String line = "";
                for (int col = 0; col < gameBoard[0].length; col++) {
                    if (gameBoard[row][col] == s.getHead()) {
                        line += "2";
                    } else if (s.getBody().contains(gameBoard[row][col])) {
                        line += "1";
                    } else if (foodPos.contains(gameBoard[row][col])) {
                        line += "3";
                    } else if (bombPos != null && bombPos.contains(gameBoard[row][col])) {
                        line += "4";
                    } else {
                        line += "0";
                    }
                }
                br.write(line);
                br.newLine();
            }
            br.flush();
            br.close();
        } catch (FileNotFoundException exception) {
            throw new FileNotFoundException("The given file could not be found");
        } catch (IOException exception) {
            throw new IOException("IOException encountered while reading file");
        }
    }

    /* This method returns an ArrayList of Tuples (containing coordinates)
     *  representing the initial snakes position
     */
    public void snakeDepthFirst(int i, int j, int[][] board, ArrayList<int[]> snakeBody) {

        int m = board.length;
        int n = board[0].length;

        if (i < m && j < n && i >= 0 && j >= 0 && board[i][j] == 1) {
            int[] bodyPart = { i, j };
            snakeBody.add(bodyPart);
            // this ensures we won't be re-adding the same body parts
            board[i][j] = -1;
            snakeDepthFirst(i + 1, j, board, snakeBody);
            snakeDepthFirst(i - 1, j, board, snakeBody);
            snakeDepthFirst(i, j + 1, board, snakeBody);
            snakeDepthFirst(i, j - 1, board, snakeBody);
            // sets it back to how it should be
            board[i][j] = 1;
        }
    }

    // This function finds the location of the top left gridBox relative to the JFrame.
    // After it then initializes all gridBoxes in the 2D Array gameBoard.
    public void setGrid() {

        int rows = this.getRow();
        int columns = this.getColumn();

        final int BOX_SIZE =
                SnakeGameCourt.BOARD_DIMENSION / (Math.max(rows, columns));

        int difference;
        if (rows == columns) {
            this.topLeftX = SnakeGameCourt.BORDER_WEIGHT;
            this.topLeftY = SnakeGameCourt.BORDER_WEIGHT;
        } else if (rows > columns) {
            this.topLeftY = SnakeGameCourt.BORDER_WEIGHT;
            difference = rows - columns;
            this.topLeftX = SnakeGameCourt.BORDER_WEIGHT + difference * BOX_SIZE / 2;
        } else {
            this.topLeftX = SnakeGameCourt.BORDER_WEIGHT;
            difference = columns - rows;
            this.topLeftY = SnakeGameCourt.BORDER_WEIGHT + difference * BOX_SIZE / 2;
        }

        // Initializing the gridBox 2D Array, which will control the game state.
        int px;
        int py;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                px = this.topLeftX + i * BOX_SIZE;
                py = this.topLeftY + j * BOX_SIZE;
                this.gameBoard[i][j] = new GridBox(i, j, px, py, BOX_SIZE);
            }
        }
    }

    // sets snake params.
    public Snake setSnake() {
        if (this.snakeBoxes == null || this.snakeBoxes.size() == 0) {
            System.out.println("Snake not correctly read in");
        }

        Snake s;

        int row;
        int column;
        GridBox head = null;
        GridBox second = null;
        LinkedList<GridBox> body = new LinkedList<>();
        boolean isHead = true;
        boolean isSecond = false;
        for (int[] position : this.snakeBoxes) {
            row = position[0];
            column = position[1];
            body.add(this.gameBoard[row][column]);

            if (isSecond) {
                second = this.gameBoard[row][column];
                isSecond = false;
            }

            if (isHead) {
                head = this.gameBoard[row][column];
                isHead = false;
                isSecond = true;
            }
        }

        // determines initial direction
        int direction;

        if (head == null) {
            return null;
        }

        if (head.getRow() == second.getRow()) {
            if (head.getColumn() > second.getColumn()) {
                direction = 1;
            } else {
                direction = 3;
            }
        } else {
            if (head.getRow() > second.getRow()) {
                direction = 2;
            } else {
                direction = 0;
            }
        }

        s = new Snake(head, body, direction);
        return s;
    }

    public Food setFood() {
        if (this.foodBoxes == null || this.foodBoxes.size() == 0) {
            System.out.println("food not correctly read in");
        }

        Food f;
        ArrayList<GridBox> foodPositions = new ArrayList<>();
        int row;
        int column;
        for (int[] position : this.foodBoxes) {
            row = position[0];
            column = position[1];
            foodPositions.add(this.gameBoard[row][column]);
        }

        f = new Food(-1, -1, -1, -1, -1, foodPositions);
        return f;
    }

    public Bomb setBomb() {
        if (this.bombBoxes == null || this.bombBoxes.size() == 0) {
            System.out.println("bomb not correctly read in");
        }
        Bomb b;
        ArrayList<GridBox> bombPositions = new ArrayList<>();
        int row;
        int column;
        for (int[] position : this.bombBoxes) {
            row = position[0];
            column = position[1];
            bombPositions.add(this.gameBoard[row][column]);
        }

        b = new Bomb(-1, -1, -1, -1, -1, bombPositions);
        return b;
    }

    public void addFood(Snake s, Food f, Bomb b, Boolean bombMode) {

        // Generating new ArrayList of all available positions.
        ArrayList<ArrayList<Integer>> gameBoardPositions = new ArrayList<>();
        ArrayList<Integer> element;
        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < this.getColumn(); j++) {
                element = new ArrayList<>();
                element.add(i);
                element.add(j);
                gameBoardPositions.add(element);
            }
        }

        // Removes all snake, food, and bomb boxes
        for (GridBox g : s.getBody()) {
            element = new ArrayList<>();
            element.add(g.getRow());
            element.add(g.getColumn());
            if (gameBoardPositions.contains(element)) {
                gameBoardPositions.remove(element);
            }
        }

        for (GridBox g : f.getPositions()) {
            element = new ArrayList<>();
            element.add(g.getRow());
            element.add(g.getColumn());
            if (gameBoardPositions.contains(element)) {
                gameBoardPositions.remove(element);
            }
        }

        if (b != null) {
            for (GridBox g : b.getPositions()) {
                element = new ArrayList<>();
                element.add(g.getRow());
                element.add(g.getColumn());
                if (gameBoardPositions.contains(element)) {
                    gameBoardPositions.remove(element);
                }
            }
        }

        int randomIndex = (int)(Math.random() * gameBoardPositions.size());
        ArrayList<Integer> positionChoice = gameBoardPositions.get(randomIndex);

        int newRow = positionChoice.get(0);
        int newColumn = positionChoice.get(1);

        GridBox newFoodBox = this.gameBoard[newRow][newColumn];
        f.addEdible(newFoodBox);

        // if in bombMode it generates a new bomb
        if (bombMode) {
            element = new ArrayList<>();
            element.add(f.getRow());
            element.add(f.getColumn());
            gameBoardPositions.remove(f);

            int rIndex = (int)(Math.random() * gameBoardPositions.size());
            ArrayList<Integer> pChoice = gameBoardPositions.get(rIndex);

            int nRow = pChoice.get(0);
            int nColumn = pChoice.get(1);

            GridBox newBombBox = this.gameBoard[nRow][nColumn];
            b.addEdible(newBombBox);
        }

    }

    @Override
    public void reset() {
        this.gameBoard = null;
        this.snakeBoxes = new ArrayList<>();
        this.foodBoxes = new ArrayList<>();
        this.bombBoxes = new ArrayList<>();
        this.topLeftX = -1;
        this.topLeftY = -1;
        this.fileBoard = null;
    }
}
