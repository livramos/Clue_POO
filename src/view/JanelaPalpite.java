package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import controller.ClueController;
import model.ClueFacade;

public class JanelaPalpite extends JFrame {

    private JComboBox<String> comboSuspeito;
    private JComboBox<String> comboArma;
    private JLabel            labelResultado;

    public JanelaPalpite(PainelTabuleiro painelTabuleiro) {
        setTitle("Palpite");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 5, 5));

        ClueFacade facade = ClueFacade.getInstancia();

        comboSuspeito = new JComboBox<String>();
        comboArma     = new JComboBox<String>();

        for (String suspeito : facade.getSuspeitos()) {
            comboSuspeito.addItem(suspeito);
        }
        for (String arma : facade.getArmas()) {
            comboArma.addItem(arma);
        }

        String comodoAtual = facade.getComodoAtualJogadorDaVez();

        JButton botaoConfirmar = new JButton("Confirmar Palpite");
        labelResultado = new JLabel(" ");

        JButton botaoMostrar = new JButton("Mostrar Carta");
        botaoMostrar.setVisible(false); // so aparece se alguem puder refutar

        botaoMostrar.addActionListener(e -> {
            String carta = ClueFacade.getInstancia().getCartaRefutada();

            String caminho = JanelaCartas.getCaminhoImagemCarta(carta);
            javax.swing.ImageIcon icone = null;
            if (caminho != null) {
                java.net.URL url = getClass().getResource(caminho);
                if (url != null) {
                    java.awt.Image img = new javax.swing.ImageIcon(url).getImage()
                            .getScaledInstance(200, 280, java.awt.Image.SCALE_SMOOTH);
                    icone = new javax.swing.ImageIcon(img);
                }
            }

            JOptionPane.showMessageDialog(
                this,
                "Carta mostrada: " + carta,
                "Refutacao",
                JOptionPane.INFORMATION_MESSAGE,
                icone
            );
        });

        botaoConfirmar.addActionListener(e -> {
            String suspeito = (String) comboSuspeito.getSelectedItem();
            String arma     = (String) comboArma.getSelectedItem();

            String resultado = ClueFacade.getInstancia().realizarPalpite(suspeito, arma);

            labelResultado.setText(resultado);

        
            if (ClueFacade.getInstancia().getCartaRefutada() != null) {
                botaoMostrar.setVisible(true);
            } else {
       
                JOptionPane.showMessageDialog(
                    this,
                    "Ninguem conseguiu refutar o seu palpite!\n"
                  + "Essas cartas podem estar no envelope confidencial.\n"
                  + "Considere fazer uma acusacao.",
                    "Palpite nao refutado",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

            botaoConfirmar.setEnabled(false);

            ClueController.getInstancia().onPalpiteConcluido();
        });

        add(new JLabel("Comodo do palpite: " + comodoAtual));
        add(new JLabel("Suspeito:"));
        add(comboSuspeito);
        add(new JLabel("Arma:"));
        add(comboArma);
        add(botaoConfirmar);
        add(labelResultado);
        add(botaoMostrar);
    }
}