package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class JanelaTabuleiro extends JFrame {

    private PainelTabuleiro painelTabuleiro;
    private PainelLateral painelLateral;

    public JanelaTabuleiro() {
        setTitle("Clue");
        setSize(1200, 900);
        setMaximumSize(new Dimension(1400, 1050));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        painelTabuleiro = new PainelTabuleiro();
        painelLateral = new PainelLateral();

        setLayout(new BorderLayout());

        add(painelTabuleiro, BorderLayout.CENTER);
        add(painelLateral, BorderLayout.EAST);
    }
}