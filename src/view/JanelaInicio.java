package view;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ClueController;

public class JanelaInicio extends JFrame {

    private Image imagemFundo;

    public JanelaInicio() {
        setTitle("Clue - Início");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        carregarImagemFundo();

        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (imagemFundo != null) {
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        painel.setLayout(null);
        setContentPane(painel);

        JButton botaoNovoJogo = new JButton("Novo Jogo");
        botaoNovoJogo.setBounds(365, 270, 170, 35);
        painel.add(botaoNovoJogo);

        JButton botaoCarregarJogo = new JButton("Carregar Jogo");
        botaoCarregarJogo.setBounds(365, 315, 170, 35);
        painel.add(botaoCarregarJogo);

        botaoNovoJogo.addActionListener(e -> abrirNovoJogo());
        botaoCarregarJogo.addActionListener(e -> carregarJogo());
    }

    private void carregarImagemFundo() {
        URL url = getClass().getResource("/imagens/Tabuleiros/tela_inicio.jpg");

        if (url != null) {
            imagemFundo = new ImageIcon(url).getImage();
        }
    }

    private void abrirNovoJogo() {
        janelaSelecao escolha = new janelaSelecao();
        escolha.setVisible(true);
        dispose();
    }

    private void carregarJogo() {
        boolean carregou = ClueController.getInstancia().onCarregarJogo(this);

        if (carregou) {
            dispose();
        }
    }
}