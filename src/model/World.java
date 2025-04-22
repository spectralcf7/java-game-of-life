package model;

import exceptions.MismatchedSizeException;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class World {
    private final int rows;
    private final int columns;
    private final boolean[][] grid;
    private final boolean[][] buffer;

    public World(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        grid = new boolean[rows][columns];
        buffer = new boolean[rows][columns];
    }

    public boolean getCell(int row, int column) {
        return grid[row][column];
    }

    public void setCell(int row, int column, boolean status) {
        grid[row][column] = status;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /* Activate several random cells */
    public void randomize() {
        Random random = new Random();

        for(int i = 0; i < (rows * columns) / 10; i++) {
            int row = random.nextInt(rows);
            int column = random.nextInt(columns);

            setCell(row, column, true);
        }
    }

    /* Reset the grid */
    public void clear() {
        for(int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], false);
        }
    }

    private int countActiveNeighbours(int row, int col) {
        int neighbours = 0;

        /* Visiting all neighbors for cell grid[row][col] - minimum: 3 (at the corners), maximum: 8 */
        for(int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for(int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                /* Compute neighbours' row and column indexes */
                int gridRow = row + rowOffset;
                int gridCol = col + colOffset;

                /* Check the edges */
                if (gridRow < 0) {
                    continue;
                } else if (gridRow == rows) {
                    continue;
                }

                if (gridCol < 0) {
                    continue;
                } else if (gridCol == columns) {
                    continue;
                }

                boolean status = getCell(gridRow, gridCol);
                if (status) {
                    neighbours++;
                }
             }
        }
        return neighbours;
    }

    /* Game logic */
    public void next() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                int activeNeighbors = countActiveNeighbours(row, col);
                System.out.println("(" + row + ", " + col + ") " + activeNeighbors);

                /*
                 * If neighbouring cell count < 2, deactivate cell
                 * If neighbouring cell count > 3, deactivate cell
                 * If neighbouring cell count == 3, activate cell
                 * If neighbouring cell count == 2, leave it be
                 *
                 * However, we have to take the whole grid into consideration,
                 * so as not to mess it up by activating/deactivating cells (create a buffer and copy it into the original)
                 */

                boolean status;

                if (activeNeighbors == 2) {
                    status = getCell(row, col);
                }
                else status = activeNeighbors == 3;

                buffer[row][col] = status;
            }
        }
        for(int row = 0; row < rows; row++) {
            if (columns >= 0) System.arraycopy(buffer[row], 0, grid[row], 0, columns);
        }
    }

    /* Serialize the grid configuration */
    public void save(File selectedFile) throws IOException {
        try(var dos = new DataOutputStream(new FileOutputStream(selectedFile))) {
            dos.writeInt(rows);
            dos.writeInt(columns);

            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < columns; col++) {
                    dos.writeBoolean(grid[row][col]);
                }
            }
        }
    }

    /* Load grid configuration */
    public void load(File selectedFile) throws IOException, MismatchedSizeException {
        try(var dis = new DataInputStream(new FileInputStream(selectedFile))) {
            int fileRows = dis.readInt();
            int fileColumns = dis.readInt();

            for(int row = 0; row < fileRows; row++) {
                for(int col = 0; col < fileColumns; col++) {
                    boolean status = dis.readBoolean();

                    if(row >= rows || col >= columns) {
                        continue;
                    }
                    grid[row][col] = status;
                }
            }

            if(fileRows != this.rows || fileColumns != this.columns) {
                throw new MismatchedSizeException();
            }
        }
    }
}
