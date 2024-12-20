import javax.swing.SwingUtilities;

import ui.ChessGUI;

public class Main {
    public static void main(String[] args) {
        // Ensure the program starts
        System.out.println("Program Started");

        // Create and show the Chess GUI
        SwingUtilities.invokeLater(() -> {
            ChessGUI gui = new ChessGUI();
            gui.setVisible(true);
        });
    }
}

