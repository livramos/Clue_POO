package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.ClueFacade;

public class JanelaAcusacao extends JFrame {

    private JComboBox<String> comboSuspeito;
    private JComboBox<String> comboArma;
    private JComboBox<String> comboComodo;
    private JLabel labelResultado;

    public JanelaAcusacao() {
        setTitle("Acusação");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 5, 5));

        ClueFacade facade = ClueFacade.getInstancia();

        comboSuspeito = new JComboBox<String>();
        comboArma = new JComboBox<String>();
        comboComodo = new JComboBox<String>();

        for (String suspeito : facade.getSuspeitos()) {
            comboSuspeito.addItem(suspeito);
        }

        for (String arma : facade.getArmas()) {
            comboArma.addItem(arma);
        }

        for (String comodo : facade.getComodos()) {
            comboComodo.addItem(comodo);
        }

        JButton botaoConfirmar = new JButton("Confirmar Acusação");
        labelResultado = new JLabel(" ");

        botaoConfirmar.addActionListener(e -> realizarAcusacao());

        add(new JLabel("Suspeito:"));
        add(comboSuspeito);
        add(new JLabel("Arma:"));
        add(comboArma);
        add(new JLabel("Cômodo:"));
        add(comboComodo);
        add(botaoConfirmar);
        add(labelResultado);
    }

    private void realizarAcusacao() {
        String suspeito = (String) comboSuspeito.getSelectedItem();
        String arma = (String) comboArma.getSelectedItem();
        String comodo = (String) comboComodo.getSelectedItem();

        boolean correta = ClueFacade.getInstancia().realizarAcusacao(
                suspeito,
                arma,
                comodo
        );

        if (correta) {
            labelResultado.setText("Acusação correta! O jogador venceu.");
        } else {
            labelResultado.setText("Acusação incorreta. O jogador não joga mais.");
        }
    }
}
