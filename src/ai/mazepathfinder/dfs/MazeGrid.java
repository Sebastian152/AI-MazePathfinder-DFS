
package ai.mazepathfinder.dfs;

/**
 *
 * @author Sebastián G.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MazeGrid extends JPanel {
    private static final int SIZE = 8; // Size of the matrix (8x8)
    private static final int CELL_SIZE = 50; // Size of each cell in pixels
    private int[][] grid; // Matrix to represent the maze
    private int startX, startY, endX, endY; // Coordinates for start and end
    private int lastRow = -1, lastCol = -1; // Last visited cell
    private List<Point> path; // List to store the valid path

    public MazeGrid(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        grid = new int[SIZE][SIZE]; // Initialize the matrix
        grid[startX][startY] = 5; // Mark the start cell as part of the path
        grid[endX][endY] = 9; // Mark the end cell as part of the path
        path = new ArrayList<>(); // Initialize the path list

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
                } else if (grid[row][col] == 2) {
                    g.setColor(Color.YELLOW); // Visited cells (yellow)
                } else {
                    g.setColor(Color.BLACK); // Wall (black)
                }
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw cell

                g.setColor(Color.RED);
                g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw cell border
            }
        }

        // Draw the path in green if it exists
        if (!path.isEmpty()) {
            g.setColor(Color.GREEN);
            for (Point p : path) {
                g.fillRect(p.y * CELL_SIZE, p.x * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw path cells
            }
        }
    }
    
    // Method to reset the grid
    public void resetGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == startX && col == startY) {
                    grid[row][col] = 5; // Restore the start cell (blue)
                } else if (row == endX && col == endY) {
                    grid[row][col] = 9; // Restore the end cell (red)
                } else {
                    grid[row][col] = 0; // Set all other cells to black (wall)
                }
            }
        }

        // Clear the path
        path.clear();

        // Redraw the panel
        repaint();
    }

    // DFS method to find a path from the start to the end
    public boolean findPathDFS() {
        boolean[][] visited = new boolean[SIZE][SIZE]; // Matrix to track visited cells
        path.clear(); // Clear the previous path

        long startTime = System.nanoTime(); // Start the timer

        boolean result = dfs(startX, startY, visited); // Call DFS from the start cell

        long endTime = System.nanoTime(); // Stop the timer
        long duration = (endTime - startTime) / 1_000_000; // Calculate the time in milliseconds

        System.out.println("Execution time: " + duration + " ms");

        return result;
    }

    private boolean dfs(int row, int col, boolean[][] visited) {
        // If the cell is out of bounds, a wall, or already visited, return false
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || grid[row][col] == 0 || visited[row][col]) {
            return false;
        }

        // Mark the cell as visited
        visited[row][col] = true;

        // Paint the explored cell yellow (in real-time)
        if (!(row == startX && col == startY) && !(row == endX && col == endY)) {
            grid[row][col] = 2; // Mark as explored
            repaint(); // Redraw the panel
            try {
                Thread.sleep(200); // Delay for real-time visualization
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // If we reach the end cell, add it to the path and return true
        if (row == endX && col == endY) {
            path.add(new Point(row, col));
            return true;
        }

        // Explore the 4 possible directions (up, down, left, right)
        if (dfs(row - 1, col, visited) || dfs(row + 1, col, visited) ||
            dfs(row, col - 1, visited) || dfs(row, col + 1, visited)) {
            path.add(new Point(row, col)); // Add the cell to the path
            return true;
        }

        return false; // If no path is found, return false
    }
    
    // Method to initiate BFS pathfinding
    public boolean findPathBFS() {
        boolean[][] visited = new boolean[SIZE][SIZE]; // Matrix to track visited cells
        path.clear(); // Clear the previous path

        long startTime = System.nanoTime(); // Start the timer

        boolean result = bfs(startX, startY, visited); // Call BFS from the start cell

        long endTime = System.nanoTime(); // Stop the timer
        long duration = (endTime - startTime) / 1_000_000; // Calculate the time in milliseconds

        System.out.println("BFS Execution time: " + duration + " ms");

        return result;
    }

    private boolean bfs(int startRow, int startCol, boolean[][] visited) {
        Queue<Point> queue = new LinkedList<>();
        Point[][] parent = new Point[SIZE][SIZE]; // To reconstruct the path

        // Mark the starting cell as visited and enqueue it
        visited[startRow][startCol] = true;
        queue.add(new Point(startRow, startCol));
        parent[startRow][startCol] = null; // Start point has no parent

        // Directions: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int row = current.x;
            int col = current.y;

            // Visualize the exploration (yellow)
            if (!(row == startX && col == startY) && !(row == endX && col == endY)) {
                grid[row][col] = 2;
                repaint();
                try {
                    Thread.sleep(200); // Delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Check if we've reached the end
            if (row == endX && col == endY) {
                // Reconstruct the path
                reconstructPath(parent);
                return true;
            }

            // Explore all 4 directions
            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];

                // Check if the new position is valid
                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && 
                    grid[newRow][newCol] != 0 && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                    queue.add(new Point(newRow, newCol));
                }
            }
        }

        return false; // No path found
    }

    // Helper method to reconstruct the path from BFS
    private void reconstructPath(Point[][] parent) {
        path.clear();
        Point current = new Point(endX, endY);

        while (current != null) {
            path.add(0, current); // Add to beginning to reverse the order
            current = parent[current.x][current.y];
        }

        // Remove the start point if you don't want it in the path
        if (!path.isEmpty() && path.get(0).x == startX && path.get(0).y == startY) {
            path.remove(0);
        }

        repaint(); // Update the visualization
    }
}