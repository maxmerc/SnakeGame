package org.cis1200.snake;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class RunSnake implements Runnable {

    public void run() {
        final JFrame frame = new JFrame("Snake Game");
        frame.setLayout(new BorderLayout());
        frame.setLocation(400, 0);
        frame.setPreferredSize(new Dimension(SnakeGameCourt.COURT_DIMENSION,
                SnakeGameCourt.COURT_DIMENSION));

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel();
        status_panel.add(status);

        // JButtons
        // The game starts when a button is pressed.

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final SnakeGameCourt court = new SnakeGameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        final JButton board1 = new JButton("Small");
        board1.addActionListener(e -> court.setLevel(1));
        control_panel.add(board1);

        final JButton board2 = new JButton("Medium");
        board2.addActionListener(e -> court.setLevel(2));
        control_panel.add(board2);

        final JButton board3 = new JButton("Large");
        board3.addActionListener(e -> court.setLevel(3));
        control_panel.add(board3);

        final JButton board4 = new JButton("Extra Large");
        board4.addActionListener(e -> court.setLevel(4));
        control_panel.add(board4);

        final JButton board5 = new JButton("Large - Bomb Mode");
        board5.addActionListener(e -> court.setLevel(5));
        control_panel.add(board5);

        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> {
            try {
                court.saveGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(save);

        final JButton load = new JButton("Load Game");
        load.addActionListener(e -> court.setLevel(6));
        control_panel.add(load);

        status.setText("Choose a game mode");

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setVisible(true);

        // Instructions
        String instructionMessage = "Welcome to Max's Snake Game! \n\n" +
                "Once you close this window, you can choose to\n" +
                "play on a small, medium, Large, or Extra Large board.\n" +
                "Click on any of these buttons to start a game. \n" +
                "Additionally, click the Bomb Mode for a special mode where avoid bombs.\n" +
                "Once you die, make sure to press Reset before you start a new game! \n" +
                "The snake is controlled by arrow keys. Good luck!\n";

        JOptionPane instructionPane = new JOptionPane();
        instructionPane.showMessageDialog(frame, instructionMessage);
    }
}
