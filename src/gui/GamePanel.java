package gui;

import exceptions.MismatchedSizeException;
import model.World;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    private final static int CELL_SIZE = 20;

    private final static Color backgroundColor = Color.BLACK;
    private final static Color foregroundColor = Color.GREEN;
    private final static Color gridColor = Color.GRAY;

    private int topBottomMargin;
    private int leftRightMargin;

    private World gameWorld;

    public GamePanel() {
        /* Listen for mouse clicks to toggle grid cells */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                /* Get the cell 'matrix' coordinates (x, y) */
                int row = (e.getY() - topBottomMargin) / CELL_SIZE;
                int col = (e.getX() - leftRightMargin) / CELL_SIZE;

                /* Check for invalid clicks */
                if(row >= gameWorld.getRows() || col >= gameWorld.getColumns()) {
                    return;
                }

                /* Toggle cell status */
                boolean toggledStatus = !gameWorld.getCell(row, col);
                gameWorld.setCell(row, col, toggledStatus);

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int width = getWidth();
        int height = getHeight();

        /* Compute all grid margins */
        leftRightMargin = ((width % CELL_SIZE) + CELL_SIZE) / 2;
        topBottomMargin = ((height % CELL_SIZE) + CELL_SIZE) / 2;

        /* Compute number of rows and columns */
        int rows = (height - 2 * topBottomMargin) / CELL_SIZE;
        int columns = (width - 2 * leftRightMargin) / CELL_SIZE;

        /* Construct the grid in case the configuration is invalid */
        if (gameWorld == null) {
            gameWorld = new World(rows, columns);
        }
        else {
            if(gameWorld.getRows() != rows || gameWorld.getColumns() != columns) {
                gameWorld = new World(rows, columns);
            }
        }

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);
        drawGrid(g2, width, height);

        /* Fill the grid cell */
        for(int col = 0; col < columns; col++) {
            for(int row = 0; row < rows; row++) {
                boolean status = gameWorld.getCell(row, col);
                fillCell(g2, row, col, status);
            }
        }
    }

    private void fillCell(Graphics2D g2, int row, int col, boolean status) {
        /* Select fill color depending on cell status */
        Color color = status ? foregroundColor : backgroundColor;
        g2.setColor(color);
        int x = leftRightMargin + col * CELL_SIZE;
        int y = topBottomMargin + row * CELL_SIZE;
        /* Fill the corresponding rectangle */
        g2.fillRect(x + 1, y + 1,CELL_SIZE - 2, CELL_SIZE - 2);
    }

    /* Draw the grid's horizontal and vertical lines */
    private void drawGrid(Graphics2D g2, int width, int height) {
        g2.setColor(gridColor);
        for(int x = leftRightMargin; x <= width - leftRightMargin; x += CELL_SIZE) {
            g2.drawLine(x, topBottomMargin, x, height - topBottomMargin);
        }

        for(int y = topBottomMargin; y <= height - topBottomMargin; y += CELL_SIZE) {
            g2.drawLine(leftRightMargin, y, width - leftRightMargin, y);
        }
    }

    /* Randomize grid */
    public void randomize() {
        gameWorld.randomize();
        repaint();
    }

    /* Reset the grid */
    public void clear() {
        gameWorld.clear();
        repaint();
    }

    /* Next grid configuration */
    public void next() {
        gameWorld.next();
        repaint();
    }

    /* Save file - redirect to World class */
    public void save(File selectedFile) {
        try {
            gameWorld.save(selectedFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot save selected file.", "An error occurred", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Open file - redirect to World class */
    public void open(File selectedFile) {
        try {
            gameWorld.load(selectedFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot open selected file.", "An error occurred", JOptionPane.ERROR_MESSAGE);
        } catch (MismatchedSizeException e) {
            JOptionPane.showMessageDialog(this, "Loading grid size from a larger or smaller grid.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        repaint();
    }
}
