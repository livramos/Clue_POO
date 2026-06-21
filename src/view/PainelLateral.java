package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.ClueController;
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
    private String  jogadorDaVez;
    private int     dado1;
    private int     dado2;

  

    public PainelLateral() {
        setPreferredSize(new Dimension(220, 900));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        jogadorDaVez = "";
        dado1 = 1;
        dado2 = 1;

        carregarImagens();
        criarBotoes();
        posicionarBotoes();
        configurarEventos();

    }


    private void carregarImagens() {
        imagensDados = new Image[7];
        imagensDados[1] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado1.jpg")).getImage();
        imagensDados[2] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado2.jpg")).getImage();
        imagensDados[3] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado3.jpg")).getImage();
        imagensDados[4] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado4.jpg")).getImage();
        imagensDados[5] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado5.jpg")).getImage();
        imagensDados[6] = new ImageIcon(getClass().getResource("/imagens/Tabuleiros/dado6.jpg")).getImage();
    }

    private void criarBotoes() {
        botaoPassagemSecreta = new JButton("Passagem Secreta");
        botaoProximo         = new JButton("Proximo");
        botaoMostrarCartas   = new JButton("Mostrar Cartas");
        botaoBlocoNotas      = new JButton("Bloco de Notas");
        botaoPalpite         = new JButton("Palpite");
        botaoAcusar          = new JButton("Acusar");
        botaoSalvarJogo      = new JButton("Salvar Jogo");
        botaoJogarDados      = new JButton("Jogar Dados");
        botaoEscolherDados   = new JButton("Escolher Dados");

       
        botaoPassagemSecreta.setEnabled(false);
        botaoProximo.setEnabled(false);
        botaoPalpite.setEnabled(false);
        botaoAcusar.setEnabled(true);
    }

    private void posicionarBotoes() {
        botaoPassagemSecreta.setBounds(20,  20, 180, 35);
        botaoProximo        .setBounds(20,  65, 180, 35);
        botaoMostrarCartas  .setBounds(20, 110, 180, 35);
        botaoBlocoNotas     .setBounds(20, 155, 180, 35);
        botaoPalpite        .setBounds(20, 200, 180, 35);
        botaoAcusar         .setBounds(20, 245, 180, 35);
        botaoSalvarJogo     .setBounds(20, 300, 180, 35);
        botaoJogarDados     .setBounds(20, 580, 180, 35);
        botaoEscolherDados  .setBounds(20, 625, 180, 35);

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
                ClueController.getInstancia().onLancarDados();
            }
        });

        botaoEscolherDados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                escolherDadosManualmente();
            }
        });

        botaoProximo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClueController.getInstancia().onPassarTurno();
            }
        });

        botaoPassagemSecreta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClueController.getInstancia().onUsarPassagemSecreta();
            }
        });

        botaoPalpite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClueController.getInstancia().onFazerPalpite();
            }
        });

        botaoAcusar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClueController.getInstancia().onFazerAcusacao();
            }
        });

        botaoMostrarCartas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCartas();
            }
        });

        botaoBlocoNotas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirBlocoNotas();
            }
        });
    }


    private void mostrarCartas() {
        ClueFacade facade = ClueFacade.getInstancia();
        facade.notificarCartasExibidas();
        new JanelaCartas(facade.getJogadorAtual(), facade.getCartasJogadorDaVez())
                .setVisible(true);
    }

    private void abrirBlocoNotas() {
        ClueFacade facade = ClueFacade.getInstancia();
        facade.notificarNotasExibidas();
        new JanelaFolhasNotas(facade.getJogadorAtual()).setVisible(true);
    } 
 
    public void exibirDados(int d1, int d2) {
        this.dado1 = d1;
        this.dado2 = d2;
        repaint();
    }
  
    
    public void atualizarJogadorDaVez(String jogadorDaVez) {
        this.jogadorDaVez = jogadorDaVez;
        repaint();
    }

    public void setBotaoJogarDadosHabilitado(boolean habilitado) {
        botaoJogarDados.setEnabled(habilitado);
    }

    public void setBotaoEscolherDadosHabilitado(boolean habilitado) {
        botaoEscolherDados.setEnabled(habilitado);
    }

    public void setBotaoPassagemSecretaHabilitado(boolean habilitado) {
        botaoPassagemSecreta.setEnabled(habilitado);
    }

    public void setBotaoProximoHabilitado(boolean habilitado) {
        botaoProximo.setEnabled(habilitado);
    }

    public void setBotaoPalpiteHabilitado(boolean habilitado) {
        botaoPalpite.setEnabled(habilitado);
    }

    public void setBotaoAcusarHabilitado(boolean habilitado) {
        botaoAcusar.setEnabled(habilitado);
    }

   
    public int getDado1()       { return dado1; }
    public int getDado2()       { return dado2; }
    public int getTotalPassos() { return dado1 + dado2; }

    

    private void escolherDadosManualmente() {
        JTextField campo1 = new JTextField("1");
        JTextField campo2 = new JTextField("1");

        JPanel painel = new JPanel(new GridLayout(2, 2, 5, 5));
        painel.add(new JLabel("Dado 1 (1-6):"));
        painel.add(campo1);
        painel.add(new JLabel("Dado 2 (1-6):"));
        painel.add(campo2);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                painel,
                "Escolher Dados",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int d1 = Integer.parseInt(campo1.getText().trim());
            int d2 = Integer.parseInt(campo2.getText().trim());

            d1 = Math.min(6, Math.max(1, d1));
            d2 = Math.min(6, Math.max(1, d2));

            ClueController.getInstancia().onLancarDadosComValores(d1, d2);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Insira numeros inteiros entre 1 e 6.",
                    "Valor invalido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        desenharAreaDosDados(g2);
    }

    private void desenharAreaDosDados(Graphics2D g2) {
        int passos = dado1 + dado2;

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(jogadorDaVez, 70, 390);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(passos + " Passo(s)", 75, 420);

        g2.drawImage(imagensDados[dado1], 20,  440, 80, 80, null);
        g2.drawImage(imagensDados[dado2], 120, 440, 80, 80, null);
    }
}