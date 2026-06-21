package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class janelaSelecao extends JFrame {

    private static final String[] PERSONAGENS = {
        "Srta. Scarlet",
        "Coronel Mostarda",
        "Sra. White",
        "Sr. Green",
        "Sra. Peacock",
        "Professor Plum"
    };

    private final List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();

    public janelaSelecao () {
        setTitle("Clue - Escolha dos Personagens");
        setSize(400, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Selecione de 3 a 6 personagens", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        JPanel painelCheck = new JPanel(new GridLayout(PERSONAGENS.length, 1));
        for (String nome : PERSONAGENS) {
            JCheckBox cb = new JCheckBox(nome);
            checkBoxes.add(cb);
            painelCheck.add(cb);
        }
        add(painelCheck, BorderLayout.CENTER);

        JButton botaoIniciar = new JButton("Iniciar Partida");
        botaoIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarPartida();
            }
        });
        add(botaoIniciar, BorderLayout.SOUTH);
    }

    private void iniciarPartida() {
        List<String> escolhidos = new ArrayList<String>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                escolhidos.add(cb.getText());
            }
        }

        if (escolhidos.size() < 3) {
            JOptionPane.showMessageDialog(this,
                "Selecione pelo menos 3 personagens.",
                "Selecao invalida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JanelaTabuleiro janela = new JanelaTabuleiro(escolhidos);
        janela.setVisible(true);
        dispose();
    }
}