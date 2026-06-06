package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import controller.ClueController;
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
        painelLateral   = new PainelLateral();

        /*
         * O Controller precisa de referências para os dois painéis
         * antes de iniciarTurno() ser chamado.
         */
        ClueController controller = ClueController.getInstancia();
        controller.setPainelLateral(painelLateral);
        controller.setPainelTabuleiro(painelTabuleiro);

        inicializarPioesNaTela();

        setLayout(new BorderLayout());
        add(painelTabuleiro, BorderLayout.CENTER);
        add(painelLateral,   BorderLayout.EAST);
    }

    // =========================================================
    // Inicialização do modelo (sem mudanças)
    // =========================================================

    private void inicializarJogo() {
        ClueFacade facade = ClueFacade.getInstancia();

        List<String> jogadores = new ArrayList<String>();
        jogadores.add("Srta. Scarlet");
        jogadores.add("Coronel Mostarda");
        jogadores.add("Sra. White");
        jogadores.add("Sr. Green");
        jogadores.add("Sra. Peacock");
        jogadores.add("Professor Plum");

        facade.prepararJogo(jogadores);

        GradeTabuleiro grade = new GradeTabuleiro();
        Set<String> corredores = grade.getCasasCorredor();

        adicionarCorredores(facade, corredores);
        adicionarComodos(facade);
        adicionarPortasComodos(facade);
        adicionarCasasIniciais(facade);

        conectarCorredores(facade, corredores);
        conectarComodos(facade);
        conectarCasasIniciais(facade);

        adicionarPassagensSecretas(facade);
        posicionarJogadoresIniciais(facade);
    }

    private void adicionarCasasIniciais(ClueFacade facade) {
        facade.adicionarCasa("INICIO_Srta. Scarlet");
        facade.adicionarCasa("INICIO_Coronel Mostarda");
        facade.adicionarCasa("INICIO_Sra. Peacock");
        facade.adicionarCasa("INICIO_Sra. White");
        facade.adicionarCasa("INICIO_Sr. Green");
        facade.adicionarCasa("INICIO_Professor Plum");
    }

    private void conectarCasasIniciais(ClueFacade facade) {
        facade.conectarCasas("INICIO_Srta. Scarlet",    "L22C7");
        facade.conectarCasas("INICIO_Coronel Mostarda", "L16C1");
        facade.conectarCasas("INICIO_Sra. Peacock",     "L5C22");
        facade.conectarCasas("INICIO_Sra. White",       "L0C9");
        facade.conectarCasas("INICIO_Sr. Green",        "L0C16");
        facade.conectarCasas("INICIO_Professor Plum",   "L18C22");
    }

    private void adicionarCorredores(ClueFacade facade, Set<String> corredores) {
        for (String casa : corredores) {
            facade.adicionarCasa(casa);
        }
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

    private void adicionarPortasComodos(ClueFacade facade) {
        facade.adicionarCasa("L3C5");
        facade.adicionarCasa("L4C5");
        facade.adicionarCasa("L6C8");
        facade.adicionarCasa("L6C15");
        facade.adicionarCasa("L3C18");
        facade.adicionarCasa("L4C18");
        facade.adicionarCasa("L7C0");
        facade.adicionarCasa("L8C17");
        facade.adicionarCasa("L17C16");
        facade.adicionarCasa("L19C6");
        facade.adicionarCasa("L18C11");
        facade.adicionarCasa("L18C12");
        facade.adicionarCasa("L21C15");
        facade.adicionarCasa("L21C18");
    }

    private void conectarCorredores(ClueFacade facade, Set<String> corredores) {
        for (String casa : corredores) {
            int linha  = extrairLinha(casa);
            int coluna = extrairColuna(casa);

            String direita = GradeTabuleiro.nomeCelula(linha, coluna + 1);
            String baixo   = GradeTabuleiro.nomeCelula(linha + 1, coluna);

            if (corredores.contains(direita)) {
                facade.conectarCasas(casa, direita);
            }

            if (corredores.contains(baixo)) {
                facade.conectarCasas(casa, baixo);
            }
        }
    }

    private void conectarComodos(ClueFacade facade) {
        facade.conectarCasas("COMODO_Cozinha",          "L3C5");
        facade.conectarCasas("COMODO_Cozinha",          "L4C5");

        facade.conectarCasas("COMODO_Sala de Musica",   "L6C8");
        facade.conectarCasas("COMODO_Sala de Musica",   "L6C15");

        facade.conectarCasas("COMODO_Jardim de Inverno","L3C18");
        facade.conectarCasas("COMODO_Jardim de Inverno","L4C18");

        facade.conectarCasas("COMODO_Sala de Jantar",   "L7C0");

        facade.conectarCasas("COMODO_Salao de Jogos",   "L8C17");

        facade.conectarCasas("COMODO_Biblioteca",       "L17C16");

        facade.conectarCasas("COMODO_Sala de Estar",    "L19C6");

        facade.conectarCasas("COMODO_Entrada",          "L18C11");
        facade.conectarCasas("COMODO_Entrada",          "L18C12");
        facade.conectarCasas("COMODO_Entrada",          "L21C15");

        facade.conectarCasas("COMODO_Escritorio",       "L21C18");
    }

    private void adicionarPassagensSecretas(ClueFacade facade) {
        facade.adicionarPassagemSecreta("COMODO_Cozinha",          "COMODO_Sala de Estar");
        facade.adicionarPassagemSecreta("COMODO_Jardim de Inverno","COMODO_Escritorio");
    }

    private void posicionarJogadoresIniciais(ClueFacade facade) {
        facade.posicionarJogador("Srta. Scarlet",    "INICIO_Srta. Scarlet");
        facade.posicionarJogador("Coronel Mostarda", "INICIO_Coronel Mostarda");
        facade.posicionarJogador("Sra. White",       "INICIO_Sra. White");
        facade.posicionarJogador("Sr. Green",        "INICIO_Sr. Green");
        facade.posicionarJogador("Sra. Peacock",     "INICIO_Sra. Peacock");
        facade.posicionarJogador("Professor Plum",   "INICIO_Professor Plum");
    }

    // =========================================================
    // Posicionamento visual inicial dos peões
    // =========================================================

    private void inicializarPioesNaTela() {
        painelTabuleiro.moverPiao("Srta. Scarlet",    "INICIO_Srta. Scarlet");
        painelTabuleiro.moverPiao("Coronel Mostarda", "INICIO_Coronel Mostarda");
        painelTabuleiro.moverPiao("Sra. White",       "INICIO_Sra. White");
        painelTabuleiro.moverPiao("Sr. Green",        "INICIO_Sr. Green");
        painelTabuleiro.moverPiao("Sra. Peacock",     "INICIO_Sra. Peacock");
        painelTabuleiro.moverPiao("Professor Plum",   "INICIO_Professor Plum");

        /*
         * Inicia o primeiro turno:
         * - define jogador da vez no painel lateral
         * - habilita os botões corretos para o estado INICIO_TURNO
         */
        ClueController.getInstancia().iniciarTurno();
    }

    // =========================================================
    // Auxiliares
    // =========================================================

    private int extrairLinha(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(1, cIndex));
    }

    private int extrairColuna(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(cIndex + 1));
    }
}