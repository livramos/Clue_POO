package view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.ClueController;
import model.ClueFacade;

public class JanelaAcusacao extends JFrame {

    private JComboBox<String> comboSuspeito;
    private JComboBox<String> comboArma;
    private JComboBox<String> comboComodo;
    private JLabel            labelResultado;

    public JanelaAcusacao() {
        setTitle("Acusacao");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 5, 5));

        ClueFacade facade = ClueFacade.getInstancia();

        comboSuspeito = new JComboBox<String>();
        comboArma     = new JComboBox<String>();
        comboComodo   = new JComboBox<String>();

        for (String suspeito : facade.getSuspeitos()) {
            comboSuspeito.addItem(suspeito);
        }
        for (String arma : facade.getArmas()) {
            comboArma.addItem(arma);
        }
        for (String comodo : facade.getComodos()) {
            comboComodo.addItem(comodo);
        }

        JButton botaoConfirmar = new JButton("Confirmar Acusacao");
        labelResultado = new JLabel(" ");

        botaoConfirmar.addActionListener(e -> realizarAcusacao(botaoConfirmar));

        add(new JLabel("Suspeito:"));
        add(comboSuspeito);
        add(new JLabel("Arma:"));
        add(comboArma);
        add(new JLabel("Comodo:"));
        add(comboComodo);
        add(botaoConfirmar);
        add(labelResultado);
    }

    private void realizarAcusacao(JButton botao) {
        String suspeito = (String) comboSuspeito.getSelectedItem();
        String arma     = (String) comboArma.getSelectedItem();
        String comodo   = (String) comboComodo.getSelectedItem();

        // realizarAcusacao dispara ACUSACAO_REALIZADA e JOGADOR_ELIMINADO se incorreta
        boolean correta = ClueFacade.getInstancia().realizarAcusacao(suspeito, arma, comodo);

        if (correta) {
            labelResultado.setText("Acusacao correta! O jogador venceu.");
        } else {
            labelResultado.setText("Acusacao incorreta. O jogador nao joga mais.");
        }

        botao.setEnabled(false);

        // Informa o controller do resultado para transitar o estado corretamente
        ClueController.getInstancia().onAcusacaoConcluida(correta);
    }
}
