package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class SnakeGameCourt extends JPanel {
    private Snake snake; // the Snake, controlled by the arrow keys
    private Food food; // the Food, the gameObject that the Snake aims to intersect
    private Bomb bomb; // the Bomb, the gameObject that the Snake will try to avoid in alt gamemode
    private Board board; // the Game Board, represented as a 2D array

    private boolean playing = false; // whether the game is running
    private boolean bombMode = false; // whether or not bombs are in the game
    private final JLabel status; // Current status text, i.e. "Running..."

    // Snake Head and Body Color
    public static final Color HEAD_COLOR = Color.BLUE;
    public static final Color BODY_COLOR = Color.CYAN;

    // Constants
    // NOTE: COURT_DIMENSION is both Width and Height of the Court
    public static final int COURT_DIMENSION = 720;
    public static final int BORDER_WEIGHT = 15;
    public static final int BOARD_DIMENSION = COURT_DIMENSION - 2 * BORDER_WEIGHT;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 150;

    public SnakeGameCourt(JLabel status) {
        this.setPreferredSize(new Dimension(BOARD_DIMENSION, BOARD_DIMENSION));
        this.setBounds(0, 0, BOARD_DIMENSION, BOARD_DIMENSION);
        // creates border around the court area, JComponent method
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // ADD KEY LISTENERS HERE
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }

                if (snake.hasMoveAvailable()) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        snake.changeDirection(3);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        snake.changeDirection(1);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        snake.changeDirection(2);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        snake.changeDirection(0);
                    }
                    snake.setMoveAvailable(false);
                }
            }
        });

        this.status = status;
    }

    /*
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            snake.setMoveAvailable(true);
            status.setText("Playing... | Score: " +
                    snake.getScore() +
                    " | Press SPACE to pause");

            // seeing if intersects body
            if (snake.intersects(snake.getBody(), 1)) {
                status.setText("Game Over! | Final Score: " +
                        snake.getScore() +
                        " | Press RESET, then select new level.");
                playing = false;
            }
            // utilizing eat method for food and bomb
            if (snake.intersects(food.getPositions(), 0)) {
                food.eat(snake, bomb, board, bombMode);
            }

            if (bombMode && snake.intersects(bomb.getPositions(), 0)) {
                status.setText("Game Over! | Final Score: " +
                        bomb.eat(snake, bomb, board, bombMode) +
                        " | Press RESET, then select new game mode.");
                playing = false;
            }

            // Snake Move
            if (snake.nextMoveAvailable(board)) {
                snake.move();
            } else {
                status.setText("Game Over! | Final Score: " +
                        snake.getScore() +
                        " | Press RESET, then select new game mode.");
                playing = false;
            }

            // Update the display
            repaint();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board != null) {
            this.board.draw(g);
        }
        if (snake != null) {
            this.snake.draw(g);
        }
        if (food != null) {
            this.food.draw(g);
        }
        if (bomb != null) {
            this.bomb.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_DIMENSION, BOARD_DIMENSION);
    }

    // method for getting snake board
    public void setLevel(int level) {
        if (playing) {
            requestFocusInWindow();
            return;
        }

        String filePath = "files/BOARD_" + level + ".txt";
        File boardFile = new File(filePath);

        this.board = new Board(boardFile);

        this.board.setGrid();
        this.snake = this.board.setSnake();
        this.food = this.board.setFood();
        if (filePath.equals("files/BOARD_5.txt")) {
            bombMode = true;
            this.bomb = this.board.setBomb();
        } else if (filePath.equals("files/BOARD_6.txt")) {
            bombMode = this.board.getBombMode();
            this.bomb = this.board.setBomb();
        } else {
            bombMode = false;
            this.bomb = null;
        }

        playing = true;
        status.setText("Playing... | Score: " + snake.getScore());

        requestFocusInWindow();
    }

    public void saveGame() throws IOException {
        playing = false;
        String filePath = "files/BOARD_6.txt";
        File boardFile = new File(filePath);
        status.setText("Game Saved -> Reset and Pick New Board | Score: " + snake.getScore());
        this.board.saveBoardFile(boardFile, snake, food, bomb, bombMode);

        requestFocusInWindow();
    }

    // reset method

    public void reset() {
        if (playing) {
            requestFocusInWindow();
            return;
        }
        status.setText("Choose a game mode");
        if (board != null) {
            board.reset();
        }
        if (snake != null) {
            snake.reset();
        }
        if (food != null) {
            food.reset();
        }
        if (bomb != null) {
            bomb.reset();
        }
        bombMode = false;
    }

}
