package app;

import config.Database;
import view.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Database.initSchema();

        // Determina a aparência pelo sistema operacional atual
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {

            }
            MainView view = new MainView();
            view.setVisible(true);
        });
    }
}
