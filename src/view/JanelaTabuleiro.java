package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import model.ClueFacade;

public class JanelaTabuleiro extends JFrame {
    private PainelTabuleiro painelTabuleiro;
    private PainelLateral painelLateral;

    public JanelaTabuleiro() {

        setTitle("Clue");

        setSize(1200, 900);

        setMaximumSize(new Dimension(1400, 1050));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        inicializarJogo();
        painelTabuleiro = new PainelTabuleiro();
        painelLateral = new PainelLateral();
        painelTabuleiro.setPainelLateral(painelLateral);
        painelLateral.setPainelTabuleiro(painelTabuleiro);

        setLayout(new BorderLayout());
        add(painelTabuleiro, BorderLayout.CENTER);
        add(painelLateral, BorderLayout.EAST);
    }

    private void inicializarJogo() {
        ClueFacade facade = ClueFacade.getInstancia();

        List<String> jogadores = new ArrayList<>();

        jogadores.add("Srta. Scarlet");
        jogadores.add("Coronel Mostarda");
        jogadores.add("Sra. Peacock");

        facade.prepararJogo(jogadores);

        GradeTabuleiro grade = new GradeTabuleiro();

        Set<String> corredores = grade.getCasasCorredor();

        for (String casa : corredores) {
            facade.adicionarCasa(casa);
        }

        for (String casa : corredores) {
            int linha = extrairLinha(casa);
            int coluna = extrairColuna(casa);

            String direita = GradeTabuleiro.nomeCelula(linha,coluna + 1);
            String baixo = GradeTabuleiro.nomeCelula(linha + 1, coluna);

            if (corredores.contains(direita)) {
                facade.conectarCasas(casa,direita);
            }

            if (corredores.contains(baixo)) {
                facade.conectarCasas(casa,baixo);
            }
        }

        adicionarConexoesComodos(facade);
        facade.posicionarJogadorAtual(GradeTabuleiro.nomeCelula(23, 7));
    }
    
    private void adicionarConexoesComodos(
            ClueFacade facade
    ) {

        String[] comodos = {

                "COMODO_Cozinha",

                "COMODO_Sala de Musica",

                "COMODO_Jardim de Inverno",

                "COMODO_Sala de Jantar",

                "COMODO_Salao de Jogos",

                "COMODO_Biblioteca",

                "COMODO_Sala de Estar",

                "COMODO_Entrada",

                "COMODO_Escritorio"
        };

        for (String comodo : comodos) {
            facade.adicionarCasa(comodo);
        }

        facade.conectarCasas(
                "COMODO_Cozinha",
                "L6C6"
        );

        facade.conectarCasas(
                "COMODO_Sala de Musica",
                "L6C8"
        );

        facade.conectarCasas(
                "COMODO_Sala de Musica",
                "L6C15"
        );

        facade.conectarCasas(
                "COMODO_Jardim de Inverno",
                "L6C16"
        );

        facade.conectarCasas(
                "COMODO_Sala de Jantar",
                "L7C7"
        );

        facade.conectarCasas(
                "COMODO_Salao de Jogos",
                "L7C16"
        );

        facade.conectarCasas(
                "COMODO_Biblioteca",
                "L15C16"
        );

        facade.conectarCasas(
                "COMODO_Sala de Estar",
                "L20C7"
        );

        facade.conectarCasas(
                "COMODO_Entrada",
                "L17C9"
        );

        facade.conectarCasas(
                "COMODO_Escritorio",
                "L20C16"
        );

        facade.conectarCasas(
                "COMODO_Cozinha",
                "COMODO_Sala de Estar"
        );

        facade.conectarCasas(
                "COMODO_Jardim de Inverno",
                "COMODO_Escritorio"
        );
    }

    private int extrairLinha(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(1, cIndex));
    }

    private int extrairColuna(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(cIndex + 1));
    }
}
