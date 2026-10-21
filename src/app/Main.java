    
package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.TelaLogin;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println(
                        "Não foi possível aplicar o tema do sistema.");
            }

            new TelaLogin().setVisible(true);
        });
    }
}
