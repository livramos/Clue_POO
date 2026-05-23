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

        inicializarPioesNaTela();

        setLayout(new BorderLayout());

        add(painelTabuleiro, BorderLayout.CENTER);
        add(painelLateral, BorderLayout.EAST);
    }

    private void inicializarJogo() {
        ClueFacade facade = ClueFacade.getInstancia();

        List<String> jogadores = new ArrayList<String>();

        jogadores.add("Srta. Scarlet");
        jogadores.add("Coronel Mostarda");
        jogadores.add("Sra. Peacock");

        facade.prepararJogo(jogadores);

        GradeTabuleiro grade = new GradeTabuleiro();

        Set<String> corredores = grade.getCasasCorredor();

        for (String casa : corredores) {
            facade.adicionarCasa(casa);
        }

        adicionarComodos(facade);
        conectarCorredores(facade, corredores);
        conectarComodos(facade);
        adicionarPassagensSecretas(facade);

        facade.posicionarJogador("Srta. Scarlet", GradeTabuleiro.nomeCelula(23, 7));
        facade.posicionarJogador("Coronel Mostarda", GradeTabuleiro.nomeCelula(0, 16));
        facade.posicionarJogador("Sra. Peacock", GradeTabuleiro.nomeCelula(7, 23));
    }

    private void inicializarPioesNaTela() {
        painelTabuleiro.moverPiao("Srta. Scarlet", GradeTabuleiro.nomeCelula(23, 7));
        painelTabuleiro.moverPiao("Coronel Mostarda", GradeTabuleiro.nomeCelula(0, 16));
        painelTabuleiro.moverPiao("Sra. Peacock", GradeTabuleiro.nomeCelula(7, 23));

        painelLateral.atualizarJogadorDaVez(
                ClueFacade.getInstancia().getJogadorAtual()
        );
    }

    private void adicionarComodos(ClueFacade facade) {
        facade.adicionarCasa("COMODO_Cozinha");
        facade.adicionarCasa("COMODO_Sala de Musica");
        facade.adicionarCasa("COMODO_Jardim de Inverno");
        facade.adicionarCasa("COMODO_Sala de Jantar");
        facade.adicionarCasa("COMODO_Salao de Jogos");
        facade.adicionarCasa("COMODO_Biblioteca");
        facade.adicionarCasa("COMODO_Sala de Estar");
        facade.adicionarCasa("COMODO_Entrada");
        facade.adicionarCasa("COMODO_Escritorio");
    }

    private void conectarCorredores(ClueFacade facade, Set<String> corredores) {
        for (String casa : corredores) {
            int linha = extrairLinha(casa);
            int coluna = extrairColuna(casa);

            String direita = GradeTabuleiro.nomeCelula(linha, coluna + 1);
            String baixo = GradeTabuleiro.nomeCelula(linha + 1, coluna);

            if (corredores.contains(direita)) {
                facade.conectarCasas(casa, direita);
            }

            if (corredores.contains(baixo)) {
                facade.conectarCasas(casa, baixo);
            }
        }
    }

    private void conectarComodos(ClueFacade facade) {
        facade.conectarCasas("COMODO_Cozinha", "L6C6");

        facade.conectarCasas("COMODO_Sala de Musica", "L6C8");
        facade.conectarCasas("COMODO_Sala de Musica", "L6C15");

        facade.conectarCasas("COMODO_Jardim de Inverno", "L6C16");

        facade.conectarCasas("COMODO_Sala de Jantar", "L7C7");

        facade.conectarCasas("COMODO_Salao de Jogos", "L7C16");

        facade.conectarCasas("COMODO_Biblioteca", "L15C16");

        facade.conectarCasas("COMODO_Sala de Estar", "L20C7");

        facade.conectarCasas("COMODO_Entrada", "L17C9");

        facade.conectarCasas("COMODO_Escritorio", "L20C16");
    }

    private void adicionarPassagensSecretas(ClueFacade facade) {
        facade.adicionarPassagemSecreta(
                "COMODO_Cozinha",
                "COMODO_Sala de Estar"
        );

        facade.adicionarPassagemSecreta(
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