package application;

import gui.MainFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}