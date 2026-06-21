package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ClueFacade;

public class JanelaFolhasNotas extends JFrame {

    private List<String> cartasDoJogador;

    public JanelaFolhasNotas(String nomeJogador) {
        setTitle("Clue - " + nomeJogador.toUpperCase() + "'S Notes");
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        ClueFacade facade = ClueFacade.getInstancia();

        cartasDoJogador = facade.getCartasJogadorDaVez();

        JPanel painelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        painelPrincipal.add(criarPainelSecao(
                "Suspeitos",
                facade.getSuspeitosFolhaJogadorDaVez()
        ));

        painelPrincipal.add(criarPainelSecao(
                "Comodos",
                facade.getComodosFolhaJogadorDaVez()
        ));

        painelPrincipal.add(criarPainelSecao(
                "Armas",
                facade.getArmasFolhaJogadorDaVez()
        ));

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private JPanel criarPainelSecao(String titulo, List<String> cartas) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));
        painel.setPreferredSize(new Dimension(150, 260));

        JPanel conteudo = new JPanel(new GridLayout(0, 1, 2, 2));
        conteudo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (String carta : cartas) {
            JCheckBox checkBox = new JCheckBox(carta);
            checkBox.setFont(new Font("Arial", Font.PLAIN, 11));

            boolean marcada =
                    ClueFacade.getInstancia().cartaEstaMarcadaNaFolhaJogadorDaVez(carta);

            checkBox.setSelected(marcada);

            if (cartasDoJogador.contains(carta)) {
                checkBox.setEnabled(false);
            } else {
                checkBox.addActionListener(e -> {
                    ClueFacade.getInstancia().definirMarcacaoCartaNaFolhaJogadorDaVez(
                            carta,
                            checkBox.isSelected()
                    );
                });
            }

            conteudo.add(checkBox);
        }

        
        painel.add(conteudo, BorderLayout.CENTER);

        return painel;
    }
}