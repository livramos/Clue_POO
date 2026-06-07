package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.ClueFacade;

public class JanelaPalpite extends JFrame {

    private JComboBox<String> comboSuspeito;
    private JComboBox<String> comboArma;
    private JLabel labelResultado;

    public JanelaPalpite(PainelTabuleiro painelTabuleiro) {
        setTitle("Palpite");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 5, 5));

        ClueFacade facade = ClueFacade.getInstancia();

        comboSuspeito = new JComboBox<String>();
        comboArma = new JComboBox<String>();

        for (String suspeito : facade.getSuspeitos()) {
            comboSuspeito.addItem(suspeito);
        }

        for (String arma : facade.getArmas()) {
            comboArma.addItem(arma);
        }

        String comodoAtual = facade.getComodoAtualJogadorDaVez();

        JButton botaoConfirmar = new JButton("Confirmar Palpite");
        labelResultado = new JLabel(" ");

        botaoConfirmar.addActionListener(e -> {
            String suspeito = (String) comboSuspeito.getSelectedItem();
            String arma = (String) comboArma.getSelectedItem();

            String resultado = ClueFacade.getInstancia().realizarPalpite(suspeito, arma);

            String casaComodo = "COMODO_" + ClueFacade.getInstancia().getComodoAtualJogadorDaVez();

            if (painelTabuleiro != null) {
                painelTabuleiro.moverPiao(suspeito, casaComodo);
            }

            labelResultado.setText(resultado);
        });

        add(new JLabel("Cômodo do palpite: " + comodoAtual));
        add(new JLabel("Suspeito:"));
        add(comboSuspeito);
        add(new JLabel("Arma:"));
        add(comboArma);
        add(botaoConfirmar);
        add(labelResultado);
    }
}
