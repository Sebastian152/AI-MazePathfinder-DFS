/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ai.mazepathfinder.dfs;

/**
 *
 * @author Sebastián G.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeGrid extends JPanel {
    private static final int SIZE = 8; // Size of the matrix (8x8)
    private static final int CELL_SIZE = 50; // Size of each cell in pixels
    private int[][] grid; // Matrix to represent the maze
    private int startX, startY, endX, endY; // Coordinates for start and end
    private int lastRow = -1, lastCol = -1; // Last visited cell

    public MazeGrid(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        grid = new int[SIZE][SIZE]; // Initialize the matrix
        grid[startX][startY] = 1; // Mark the start cell as part of the path

        // Configure the panel
        setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY()); // Handle user clicks
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleClick(e.getX(), e.getY()); // Handle mouse drag
            }
        });
    }

    private void handleClick(int x, int y) {
        int row = y / CELL_SIZE; // Calculate row based on click position
        int col = x / CELL_SIZE; // Calculate column based on click position

        // Check if the current cell is different from the last visited cell
        if (row != lastRow || col != lastCol) {
            if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                if (grid[row][col] == 0) {
                    grid[row][col] = 1; // Change to path (white)
                } else if (grid[row][col] == 1) {
                    grid[row][col] = 0; // Change to wall (black)
                }
                repaint(); // Redraw the panel
            }
            // Update the last visited cell
            lastRow = row;
            lastCol = col;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == startX && col == startY) {
                    g.setColor(Color.BLUE); // Start cell (blue)
                } else if (row == endX && col == endY) {
                    g.setColor(Color.RED); // End cell (red)
                } else if (grid[row][col] == 1) {
                    g.setColor(Color.WHITE); // Path (white)
                } else {
                    g.setColor(Color.BLACK); // Wall (black)
                }
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw cell

                g.setColor(Color.RED);
                g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw cell border
            }
        }
    }

    // Test
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Grid");
        MazeGrid mazeGrid = new MazeGrid(0, 0, 4, 4); // Start at position (0, 0), end at (4, 4)
        frame.add(mazeGrid);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}