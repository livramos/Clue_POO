package view;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JanelaTabuleiro janela = new JanelaTabuleiro();
                janela.setVisible(true);
            }
        });
    }
}