package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainFrame extends JFrame {
    private final GamePanel gamePanel = new GamePanel();

    private static final String defaultFileName = "gameoflife.gol";

    public MainFrame() {
        super("Game of Life");

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);

        /* Menu Items - Save and Load */
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");

        Menu fileMenu = new Menu("File");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);

        setMenuBar(menuBar);

        JFileChooser fileChooser = new JFileChooser();

        /* Choose whatever extension for the file - serialization of the game configuration */
        var filter = new FileNameExtensionFilter("Game of Life Files", "gol");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);

        /* Action listener - clicking on the Open option */
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /* Choose file for loading */
                int userOption = fileChooser.showOpenDialog(MainFrame.this);
                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    /* Redirect to game panel */
                    gamePanel.open(selectedFile);
                }
            }
        });

        /* Action listener - clicking on the Save option */
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Set the default file name */
                fileChooser.setSelectedFile(new File(defaultFileName));

                /* Choose where to save game configuration */
                int userOption = fileChooser.showSaveDialog(MainFrame.this);

                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    /* Redirect to game panel */
                    gamePanel.save(selectedFile);
                }
            }
        });

        /* Listen for key presses */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch(code) {
                    case 32:
                        /* Space key - compute next move */
                        gamePanel.next();
                        break;
                    case 8:
                        /* Backspace key - reset the grid */
                        gamePanel.clear();
                        break;
                    case 10:
                        /* Enter key - randomize the grid */
                        gamePanel.randomize();
                        break;
                }
            }
        });

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
