package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PainelTabuleiro extends JPanel {
    private Image imagemTabuleiro;

    private static final int TABULEIRO_X = 20;
    private static final int TABULEIRO_Y = 20;
    private static final int TABULEIRO_LARGURA = 850;
    private static final int TABULEIRO_ALTURA = 820;

    public PainelTabuleiro() {
        setBackground(Color.GRAY);

        imagemTabuleiro = new ImageIcon("imagens/Tabuleiros/Tabuleiro-Original.JPG").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        desenharFundo(g2);
        desenharTabuleiro(g2);
    }

    private void desenharFundo(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void desenharTabuleiro(Graphics2D g2) {
        g2.drawImage(
            imagemTabuleiro,
            TABULEIRO_X,
            TABULEIRO_Y,
            TABULEIRO_LARGURA,
            TABULEIRO_ALTURA,
            null
        );
    }
}