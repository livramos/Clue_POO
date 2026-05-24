package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.ClueFacade;

public class PainelLateral extends JPanel {

    private JButton botaoPassagemSecreta;
    private JButton botaoProximo;
    private JButton botaoMostrarCartas;
    private JButton botaoBlocoNotas;
    private JButton botaoPalpite;
    private JButton botaoAcusar;
    private JButton botaoSalvarJogo;
    private JButton botaoJogarDados;
    private JButton botaoEscolherDados;

    private Image[] imagensDados;

    private String jogadorDaVez;
    private int dado1;
    private int dado2;
    private boolean dadosJaLancados;

    private PainelTabuleiro painelTabuleiro;

    public PainelLateral() {
        setPreferredSize(new Dimension(220, 900));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        jogadorDaVez = ClueFacade.getInstancia().getJogadorAtual();
        dado1 = 1;
        dado2 = 1;
        dadosJaLancados = false;

        carregarImagens();
        criarBotoes();
        posicionarBotoes();
        configurarEventos();
        atualizarEstadoPassagemSecreta();
    }

    public void setPainelTabuleiro(PainelTabuleiro painelTabuleiro) {
        this.painelTabuleiro = painelTabuleiro;
    }

    public void onMovimentoConcluido() {
        dadosJaLancados = false;

        botaoJogarDados.setEnabled(false);
        botaoPassagemSecreta.setEnabled(false);
        botaoProximo.setEnabled(true);

        repaint();
    }

    private void carregarImagens() {
        imagensDados = new Image[7];

        imagensDados[1] = new ImageIcon("imagens/Tabuleiros/dado1.jpg").getImage();
        imagensDados[2] = new ImageIcon("imagens/Tabuleiros/dado2.jpg").getImage();
        imagensDados[3] = new ImageIcon("imagens/Tabuleiros/dado3.jpg").getImage();
        imagensDados[4] = new ImageIcon("imagens/Tabuleiros/dado4.jpg").getImage();
        imagensDados[5] = new ImageIcon("imagens/Tabuleiros/dado5.jpg").getImage();
        imagensDados[6] = new ImageIcon("imagens/Tabuleiros/dado6.jpg").getImage();
    }

    private void criarBotoes() {
        botaoPassagemSecreta = new JButton("Passagem Secreta");
        botaoProximo = new JButton("Próximo");
        botaoMostrarCartas = new JButton("Mostrar Cartas");
        botaoBlocoNotas = new JButton("Bloco de Notas");
        botaoPalpite = new JButton("Palpite");
        botaoAcusar = new JButton("Acusar");
        botaoSalvarJogo = new JButton("Salvar Jogo");
        botaoJogarDados = new JButton("Jogar Dados");
        botaoEscolherDados = new JButton("Escolher Dados");

        botaoProximo.setEnabled(false);
        botaoPalpite.setEnabled(false);
        botaoAcusar.setEnabled(false);
        botaoEscolherDados.setEnabled(false);
    }

    private void posicionarBotoes() {
        botaoPassagemSecreta.setBounds(20, 20, 180, 35);
        botaoProximo.setBounds(20, 65, 180, 35);
        botaoMostrarCartas.setBounds(20, 110, 180, 35);
        botaoBlocoNotas.setBounds(20, 155, 180, 35);
        botaoPalpite.setBounds(20, 200, 180, 35);
        botaoAcusar.setBounds(20, 245, 180, 35);
        botaoSalvarJogo.setBounds(20, 300, 180, 35);

        botaoJogarDados.setBounds(20, 580, 180, 35);
        botaoEscolherDados.setBounds(20, 625, 180, 35);

        add(botaoPassagemSecreta);
        add(botaoProximo);
        add(botaoMostrarCartas);
        add(botaoBlocoNotas);
        add(botaoPalpite);
        add(botaoAcusar);
        add(botaoSalvarJogo);
        add(botaoJogarDados);
        add(botaoEscolherDados);
    }

    private void configurarEventos() {
        botaoJogarDados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jogarDados();
            }
        });

        botaoProximo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passarTurno();
            }
        });

        botaoPassagemSecreta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usarPassagemSecreta();
            }
        });
    }

    private void jogarDados() {
        if (dadosJaLancados) {
            return;
        }

        ClueFacade facade = ClueFacade.getInstancia();

        int[] resultado = facade.lancarDados();

        dado1 = resultado[0];
        dado2 = resultado[1];
        dadosJaLancados = true;

        botaoJogarDados.setEnabled(false);
        botaoPassagemSecreta.setEnabled(false);

        String casaAtual = facade.getCasaAtualDoJogador();

        if (casaAtual != null && painelTabuleiro != null) {
            List<String> alcancaveis =
                    facade.getCasasAlcancaveis(casaAtual, resultado);

            painelTabuleiro.destacarCasasAlcancaveis(alcancaveis);
        }

        repaint();
    }

    private void passarTurno() {
        ClueFacade facade = ClueFacade.getInstancia();

        facade.passarTurno();

        jogadorDaVez = facade.getJogadorAtual();

        dado1 = 1;
        dado2 = 1;
        dadosJaLancados = false;

        botaoProximo.setEnabled(false);
        botaoJogarDados.setEnabled(true);

        if (painelTabuleiro != null) {
            painelTabuleiro.limparDestaques();
        }

        atualizarEstadoPassagemSecreta();

        repaint();
    }
    
    private void usarPassagemSecreta() {
        try {
            ClueFacade facade = ClueFacade.getInstancia();

            String jogador = facade.getJogadorAtual();

            facade.usarPassagemSecretaJogadorDaVez();

            String destino = facade.getCasaAtualDoJogador();

            if (painelTabuleiro != null) {
                painelTabuleiro.moverPiao(jogador, destino);
                painelTabuleiro.limparDestaques();
            }

            onMovimentoConcluido();

        } catch (IllegalArgumentException ex) {
            // Se não puder usar passagem secreta, não faz nada.
        }
    }

    private void atualizarEstadoPassagemSecreta() {
        botaoPassagemSecreta.setEnabled(
                ClueFacade.getInstancia().jogadorDaVezTemPassagemSecreta()
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharAreaDosDados(g2);
    }

    private void desenharAreaDosDados(Graphics2D g2) {
        int passos = dado1 + dado2;

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(jogadorDaVez, 70, 390);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(passos + " Passo(s)", 75, 420);

        g2.drawImage(imagensDados[dado1], 20, 440, 80, 80, null);
        g2.drawImage(imagensDados[dado2], 120, 440, 80, 80, null);
    }

    public void atualizarJogadorDaVez(String jogadorDaVez) {
        this.jogadorDaVez = jogadorDaVez;
        repaint();
    }

    public int getTotalPassos() {
        return dado1 + dado2;
    }

    public int getDado1() {
        return dado1;
    }

    public int getDado2() {
        return dado2;
    }
}